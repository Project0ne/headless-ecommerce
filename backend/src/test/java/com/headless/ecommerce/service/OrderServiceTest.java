package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.OrderCreateRequest;
import com.headless.ecommerce.dto.response.CartItemResponse;
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
import com.headless.ecommerce.model.enums.ProductStatus;
import com.headless.ecommerce.model.enums.UserRole;
import com.headless.ecommerce.repository.OrderRepository;
import com.headless.ecommerce.repository.ProductRepository;
import com.headless.ecommerce.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService Tests")
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User testUser;
    private Product testProduct;
    private Order testOrder;
    private OrderCreateRequest createRequest;
    private CartItemResponse cartItemResponse;
    private final Long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .username("testuser")
            .role(UserRole.BUYER)
            .build();

        testProduct = Product.builder()
            .id(1L)
            .name("Test Product")
            .price(new BigDecimal("99.99"))
            .stock(100)
            .imageUrl("/images/test.jpg")
            .status(ProductStatus.ON_SHELF)
            .salesCount(10)
            .build();

        createRequest = OrderCreateRequest.builder()
            .receiverName("John Doe")
            .receiverPhone("1234567890")
            .receiverAddress("123 Main St")
            .build();

        cartItemResponse = CartItemResponse.builder()
            .productId(1L)
            .productName("Test Product")
            .productImage("/images/test.jpg")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(2)
            .available(true)
            .stock(100)
            .build();

        OrderItem orderItem = OrderItem.builder()
            .id(1L)
            .productId(1L)
            .productName("Test Product")
            .productImage("/images/test.jpg")
            .unitPrice(new BigDecimal("99.99"))
            .quantity(2)
            .subtotal(new BigDecimal("199.98"))
            .build();

        testOrder = Order.builder()
            .id(1L)
            .orderNo("ORD202501011200001234")
            .user(testUser)
            .totalAmount(new BigDecimal("199.98"))
            .status(OrderStatus.PENDING_PAYMENT)
            .receiverName("John Doe")
            .receiverPhone("1234567890")
            .receiverAddress("123 Main St")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        testOrder.addOrderItem(orderItem);
    }

    @Nested
    @DisplayName("Create Order Tests")
    class CreateOrderTests {

        @Test
        @DisplayName("Should create order successfully")
        void createOrder_success() {
            when(cartService.getCart(USER_ID)).thenReturn(List.of(cartItemResponse));
            when(productRepository.findAllByIdIn(List.of(1L))).thenReturn(List.of(testProduct));
            when(userService.findById(USER_ID)).thenReturn(testUser);
            when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder()
                    .id(1L).productId(1L).productName("Test Product")
                    .unitPrice(new BigDecimal("99.99")).quantity(2)
                    .subtotal(new BigDecimal("199.98")).build());

            OrderResponse response = orderService.createOrder(USER_ID, createRequest);

            assertThat(response).isNotNull();
            assertThat(response.getTotalAmount()).isEqualByComparingTo(new BigDecimal("199.98"));
            assertThat(response.getStatus()).isEqualTo("PENDING_PAYMENT");

            // Verify stock deducted
            assertThat(testProduct.getStock()).isEqualTo(98);
            assertThat(testProduct.getSalesCount()).isEqualTo(12);

            // Verify cart cleared
            verify(cartService).clearCart(USER_ID);
        }

        @Test
        @DisplayName("Should throw exception when cart is empty")
        void createOrder_emptyCart_throwsException() {
            when(cartService.getCart(USER_ID)).thenReturn(List.of());

            assertThatThrownBy(() -> orderService.createOrder(USER_ID, createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Cart is empty");
        }

        @Test
        @DisplayName("Should throw exception when cart item is unavailable")
        void createOrder_unavailableItem_throwsException() {
            CartItemResponse unavailableItem = CartItemResponse.builder()
                .productId(1L)
                .productName("Unavailable Product")
                .available(false)
                .build();

            when(cartService.getCart(USER_ID)).thenReturn(List.of(unavailableItem));

            assertThatThrownBy(() -> orderService.createOrder(USER_ID, createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("no longer available");
        }

        @Test
        @DisplayName("Should throw exception when stock is insufficient")
        void createOrder_insufficientStock_throwsException() {
            CartItemResponse largeQtyItem = CartItemResponse.builder()
                .productId(1L)
                .productName("Test Product")
                .quantity(200)
                .available(true)
                .stock(100)
                .build();

            when(cartService.getCart(USER_ID)).thenReturn(List.of(largeQtyItem));
            when(productRepository.findAllByIdIn(List.of(1L))).thenReturn(List.of(testProduct));

            assertThatThrownBy(() -> orderService.createOrder(USER_ID, createRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Insufficient stock");
        }
    }

    @Nested
    @DisplayName("Order Status Transition Tests")
    class OrderStatusTests {

        @Test
        @DisplayName("Should transition from PENDING_PAYMENT to PENDING_SHIPMENT (payment)")
        void updateOrderStatus_payment_success() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder().id(1L).productId(1L).build());

            OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.PENDING_SHIPMENT);

            assertThat(response).isNotNull();
            verify(orderRepository).save(argThat(order ->
                order.getStatus() == OrderStatus.PENDING_SHIPMENT &&
                order.getPaidAt() != null));
        }

        @Test
        @DisplayName("Should transition from PENDING_SHIPMENT to SHIPPING")
        void updateOrderStatus_ship_success() {
            testOrder.setStatus(OrderStatus.PENDING_SHIPMENT);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder().id(1L).productId(1L).build());

            OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.SHIPPING);

            assertThat(response).isNotNull();
            verify(orderRepository).save(argThat(order ->
                order.getStatus() == OrderStatus.SHIPPING));
        }

        @Test
        @DisplayName("Should transition from SHIPPING to COMPLETED")
        void updateOrderStatus_complete_success() {
            testOrder.setStatus(OrderStatus.SHIPPING);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder().id(1L).productId(1L).build());

            OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.COMPLETED);

            assertThat(response).isNotNull();
            verify(orderRepository).save(argThat(order ->
                order.getStatus() == OrderStatus.COMPLETED));
        }

        @Test
        @DisplayName("Should throw exception for invalid transition PENDING_SHIPMENT to COMPLETED")
        void updateOrderStatus_invalidTransition_throwsException() {
            testOrder.setStatus(OrderStatus.PENDING_SHIPMENT);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

            assertThatThrownBy(() -> orderService.updateOrderStatus(1L, OrderStatus.COMPLETED))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid status transition");
        }

        @Test
        @DisplayName("Should throw exception for transition from COMPLETED")
        void updateOrderStatus_fromCompleted_throwsException() {
            testOrder.setStatus(OrderStatus.COMPLETED);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

            assertThatThrownBy(() -> orderService.updateOrderStatus(1L, OrderStatus.CANCELLED))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid status transition");
        }

        @Test
        @DisplayName("Should throw exception for transition from CANCELLED")
        void updateOrderStatus_fromCancelled_throwsException() {
            testOrder.setStatus(OrderStatus.CANCELLED);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

            assertThatThrownBy(() -> orderService.updateOrderStatus(1L, OrderStatus.PENDING_SHIPMENT))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Invalid status transition");
        }
    }

    @Nested
    @DisplayName("Cancel Order Tests")
    class CancelOrderTests {

        @Test
        @DisplayName("Should cancel PENDING_PAYMENT order and restore stock")
        void cancelOrder_success() {
            // Set product stock to already-deducted state
            testProduct.setStock(98);
            testProduct.setSalesCount(12);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

            OrderResponse response = orderService.cancelOrder(1L, USER_ID);

            assertThat(response).isNotNull();
            verify(orderRepository).save(argThat(order ->
                order.getStatus() == OrderStatus.CANCELLED));
            // Stock should be restored
            verify(productRepository).save(argThat(product ->
                product.getStock() == 100 && product.getSalesCount() == 10));
        }

        @Test
        @DisplayName("Should throw exception when cancelling another user's order")
        void cancelOrder_wrongUser_throwsException() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

            assertThatThrownBy(() -> orderService.cancelOrder(1L, 999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("only cancel your own orders");
        }

        @Test
        @DisplayName("Should throw exception when cancelling non-PENDING_PAYMENT order")
        void cancelOrder_wrongStatus_throwsException() {
            testOrder.setStatus(OrderStatus.PENDING_SHIPMENT);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

            assertThatThrownBy(() -> orderService.cancelOrder(1L, USER_ID))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("PENDING_PAYMENT status can be cancelled");
        }

        @Test
        @DisplayName("Should cancel and restore stock via admin updateOrderStatus")
        void updateOrderStatus_cancel_restoresStock() {
            testProduct.setStock(98);
            testProduct.setSalesCount(12);

            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderRepository.save(any(Order.class))).thenReturn(testOrder);
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder().id(1L).productId(1L).build());

            orderService.updateOrderStatus(1L, OrderStatus.CANCELLED);

            verify(productRepository).save(argThat(product ->
                product.getStock() == 100 && product.getSalesCount() == 10));
        }
    }

    @Nested
    @DisplayName("Order Query Tests")
    class OrderQueryTests {

        @Test
        @DisplayName("Should get user orders with pagination")
        void getUserOrders_success() {
            Page<Order> orderPage = new PageImpl<>(List.of(testOrder));
            when(orderRepository.findByUserIdOrderByCreatedAtDesc(eq(USER_ID), any(Pageable.class)))
                .thenReturn(orderPage);
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder().id(1L).productId(1L).build());

            PageResponse<OrderResponse> response = orderService.getUserOrders(USER_ID, 0, 12);

            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(1);
        }

        @Test
        @DisplayName("Should get order by ID for owner")
        void getOrderById_success() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder().id(1L).productId(1L).build());

            OrderResponse response = orderService.getOrderById(1L, USER_ID);

            assertThat(response).isNotNull();
            assertThat(response.getOrderNo()).isEqualTo("ORD202501011200001234");
        }

        @Test
        @DisplayName("Should throw exception when viewing another user's order")
        void getOrderById_wrongUser_throwsException() {
            when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

            assertThatThrownBy(() -> orderService.getOrderById(1L, 999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("only view your own orders");
        }

        @Test
        @DisplayName("Should throw exception when order not found")
        void getOrderById_notFound_throwsException() {
            when(orderRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> orderService.getOrderById(999L, USER_ID))
                .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Should get all orders for admin")
        void getAllOrders_success() {
            Page<Order> orderPage = new PageImpl<>(List.of(testOrder));
            when(orderRepository.findAllByOrderByCreatedAtDesc(any(Pageable.class)))
                .thenReturn(orderPage);
            when(orderMapper.toItemResponse(any(OrderItem.class))).thenReturn(
                OrderItemResponse.builder().id(1L).productId(1L).build());

            PageResponse<OrderResponse> response = orderService.getAllOrders(0, 12);

            assertThat(response).isNotNull();
            assertThat(response.getContent()).hasSize(1);
        }
    }
}
