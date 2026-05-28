package com.headless.ecommerce.scheduler;

import com.headless.ecommerce.model.Order;
import com.headless.ecommerce.model.enums.OrderStatus;
import com.headless.ecommerce.repository.OrderRepository;
import com.headless.ecommerce.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler that automatically cancels orders pending payment for over 30 minutes.
 */
@Component
public class OrderTimeoutScheduler {

    private static final Logger logger = LoggerFactory.getLogger(OrderTimeoutScheduler.class);
    private static final int TIMEOUT_MINUTES = 30;

    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderTimeoutScheduler(OrderRepository orderRepository,
                                  OrderService orderService) {
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    /**
     * Runs every 60 seconds to check for timed-out orders.
     */
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cancelTimeoutOrders() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(TIMEOUT_MINUTES);
        List<Order> timeoutOrders = orderRepository
            .findByStatusAndCreatedAtBefore(OrderStatus.PENDING_PAYMENT, cutoffTime);

        if (!timeoutOrders.isEmpty()) {
            logger.info("Found {} timed-out orders to cancel", timeoutOrders.size());

            for (Order order : timeoutOrders) {
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);

                // Restore stock using shared service method
                orderService.restoreStock(order);

                logger.info("Cancelled timed-out order: {}", order.getOrderNo());
            }
        }
    }
}
