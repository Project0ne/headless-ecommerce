package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.request.OrderCreateRequest;
import com.headless.ecommerce.dto.response.OrderItemResponse;
import com.headless.ecommerce.dto.response.OrderResponse;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.mapper.OrderMapper;
import com.headless.ecommerce.model.Order;
import com.headless.ecommerce.model.OrderItem;
import com.headless.ecommerce.model.Product;
import com.headless.ecommerce.model.User;
import com.headless.ecommerce.model.enums.OrderStatus;
import com.headless.ecommerce.repository.OrderRepository;
import com.headless.ecommerce.repository.ProductRepository;
import com.headless.ecommerce.service.CartService;
import com.headless.ecommerce.service.OrderService;
import com.headless.ecommerce.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of OrderService for order management.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                             ProductRepository productRepository,
                             CartService cartService,
                             UserService userService,
                             OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(Long userId, OrderCreateRequest request) {
        // Get cart items
        var cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            throw new BusinessException("Cart is empty, cannot create order");
        }

        // Validate all cart items are available
        for (var item : cartItems) {
            if (!item.getAvailable()) {
                throw new BusinessException("Product '" + item.getProductName() + "' is no longer available");
            }
        }

        // Get products with pessimistic lock for stock deduction
        List<Long> productIds = cartItems.stream()
            .map(item -> item.getProductId())
            .toList();
        List<Product> products = productRepository.findAllByIdIn(productIds);

        // Validate and deduct stock
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (var cartItem : cartItems) {
            Product product = products.stream()
                .filter(p -> p.getId().equals(cartItem.getProductId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Product not found: " + cartItem.getProductId()));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("Insufficient stock for '" + product.getName()
                    + "'. Available: " + product.getStock());
            }

            // Deduct stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            product.setSalesCount(product.getSalesCount() + cartItem.getQuantity());

            totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // Generate order number
        String orderNo = generateOrderNo();

        // Get user
        User user = userService.findById(userId);

        // Create order
        Order order = Order.builder()
            .orderNo(orderNo)
            .user(user)
            .totalAmount(totalAmount)
            .status(OrderStatus.PENDING_PAYMENT)
            .receiverName(request.getReceiverName())
            .receiverPhone(request.getReceiverPhone())
            .receiverAddress(request.getReceiverAddress())
            .build();

        // Create order items
        for (var cartItem : cartItems) {
            Product product = products.stream()
                .filter(p -> p.getId().equals(cartItem.getProductId()))
                .findFirst().orElseThrow();

            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            OrderItem orderItem = OrderItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productImage(product.getImageUrl())
                .unitPrice(product.getPrice())
                .quantity(cartItem.getQuantity())
                .subtotal(subtotal)
                .build();
            order.addOrderItem(orderItem);
        }

        Order savedOrder = orderRepository.save(order);

        // Clear cart after order creation
        cartService.clearCart(userId);

        return toOrderResponse(savedOrder);
    }

    @Override
    public PageResponse<OrderResponse> getUserOrders(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orderPage = orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);

        List<OrderResponse> content = orderPage.getContent().stream()
            .map(this::toOrderResponse)
            .toList();

        return PageResponse.of(content, orderPage.getTotalElements(),
            orderPage.getTotalPages(), orderPage.getNumber(), orderPage.getSize());
    }

    @Override
    public OrderResponse getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException("You can only view your own orders");
        }

        return toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessException("You can only cancel your own orders");
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("Only orders with PENDING_PAYMENT status can be cancelled");
        }

        // Restore stock
        restoreStock(order);

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        return toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        validateStatusTransition(order.getStatus(), newStatus);

        if (newStatus == OrderStatus.PENDING_SHIPMENT) {
            order.setPaidAt(LocalDateTime.now());
        }

        if (newStatus == OrderStatus.CANCELLED) {
            restoreStock(order);
        }

        order.setStatus(newStatus);
        Order savedOrder = orderRepository.save(order);

        return toOrderResponse(savedOrder);
    }

    @Override
    public PageResponse<OrderResponse> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orderPage = orderRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<OrderResponse> content = orderPage.getContent().stream()
            .map(this::toOrderResponse)
            .toList();

        return PageResponse.of(content, orderPage.getTotalElements(),
            orderPage.getTotalPages(), orderPage.getNumber(), orderPage.getSize());
    }

    /**
     * Validates that the status transition is allowed.
     *
     * @param current the current order status
     * @param target the target order status
     */
    private void validateStatusTransition(OrderStatus current, OrderStatus target) {
        boolean valid = switch (current) {
            case PENDING_PAYMENT -> target == OrderStatus.PENDING_SHIPMENT || target == OrderStatus.CANCELLED;
            case PENDING_SHIPMENT -> target == OrderStatus.SHIPPING;
            case SHIPPING -> target == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            throw new BusinessException("Invalid status transition from " + current + " to " + target);
        }
    }

    /**
     * Restores stock for all items in a cancelled order.
     * Public method to allow reuse by OrderTimeoutScheduler.
     *
     * @param order the cancelled order
     */
    @Override
    @Transactional
    public void restoreStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            if (item.getProductId() != null) {
                productRepository.findById(item.getProductId()).ifPresent(product -> {
                    product.setStock(product.getStock() + item.getQuantity());
                    product.setSalesCount(Math.max(0, product.getSalesCount() - item.getQuantity()));
                    productRepository.save(product);
                });
            }
        }
    }

    /**
     * Generates a unique order number using timestamp + UUID prefix.
     *
     * @return the generated order number
     */
    private String generateOrderNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String uuidPrefix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD" + timestamp + uuidPrefix;
    }

    /**
     * Converts an Order entity to an OrderResponse.
     *
     * @param order the Order entity
     * @return the OrderResponse DTO
     */
    private OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
            .map(orderMapper::toItemResponse)
            .toList();

        return OrderResponse.builder()
            .id(order.getId())
            .orderNo(order.getOrderNo())
            .totalAmount(order.getTotalAmount())
            .status(order.getStatus().name())
            .receiverName(order.getReceiverName())
            .receiverPhone(order.getReceiverPhone())
            .receiverAddress(order.getReceiverAddress())
            .paidAt(order.getPaidAt() != null ? order.getPaidAt().toString() : null)
            .createdAt(order.getCreatedAt() != null ? order.getCreatedAt().toString() : null)
            .updatedAt(order.getUpdatedAt() != null ? order.getUpdatedAt().toString() : null)
            .orderItems(items)
            .build();
    }
}
