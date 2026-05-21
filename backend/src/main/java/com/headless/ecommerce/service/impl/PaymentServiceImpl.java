package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.response.OrderResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.model.Order;
import com.headless.ecommerce.model.enums.OrderStatus;
import com.headless.ecommerce.repository.OrderRepository;
import com.headless.ecommerce.service.OrderService;
import com.headless.ecommerce.service.PaymentService;
import com.headless.ecommerce.service.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of PaymentService using the Strategy pattern.
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final PaymentStrategy paymentStrategy;

    public PaymentServiceImpl(OrderRepository orderRepository,
                               OrderService orderService,
                               PaymentStrategy paymentStrategy) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.paymentStrategy = paymentStrategy;
    }

    @Override
    @Transactional
    public OrderResponse processPayment(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
            .orElseThrow(() -> new BusinessException("Order not found: " + orderNo));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new BusinessException("Order is not in PENDING_PAYMENT status");
        }

        // Process payment using strategy
        PaymentStrategy.PaymentResult result = paymentStrategy.pay(orderNo, order.getTotalAmount());

        if (!result.success()) {
            throw new BusinessException("Payment failed: " + result.message());
        }

        // Update order status to PENDING_SHIPMENT
        return orderService.updateOrderStatus(order.getId(), OrderStatus.PENDING_SHIPMENT);
    }
}
