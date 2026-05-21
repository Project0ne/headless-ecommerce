package com.headless.ecommerce.scheduler;

import com.headless.ecommerce.model.Order;
import com.headless.ecommerce.model.OrderItem;
import com.headless.ecommerce.model.enums.OrderStatus;
import com.headless.ecommerce.repository.OrderRepository;
import com.headless.ecommerce.repository.ProductRepository;
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
    private final ProductRepository productRepository;

    public OrderTimeoutScheduler(OrderRepository orderRepository,
                                  ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
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

                // Restore stock
                restoreStock(order);

                logger.info("Cancelled timed-out order: {}", order.getOrderNo());
            }
        }
    }

    /**
     * Restores stock for all items in a cancelled order.
     *
     * @param order the cancelled order
     */
    private void restoreStock(Order order) {
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
}
