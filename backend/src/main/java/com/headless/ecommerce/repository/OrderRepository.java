package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Order;
import com.headless.ecommerce.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Order entity operations.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds an order by its order number.
     *
     * @param orderNo the order number
     * @return an Optional containing the order if found
     */
    Optional<Order> findByOrderNo(String orderNo);

    /**
     * Finds all orders for a given user with pagination.
     *
     * @param userId the user ID
     * @param pageable the pagination info
     * @return a page of orders
     */
    Page<Order> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Finds orders by status with pagination (for admin).
     *
     * @param status the order status
     * @param pageable the pagination info
     * @return a page of orders
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Finds all orders ordered by creation date descending (for admin).
     *
     * @param pageable the pagination info
     * @return a page of orders
     */
    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Finds orders that are pending payment and created before the given time.
     * Used by the order timeout scheduler.
     *
     * @param status the order status (PENDING_PAYMENT)
     * @param cutoffTime the cutoff time
     * @return the list of timed-out orders
     */
    List<Order> findByStatusAndCreatedAtBefore(OrderStatus status, LocalDateTime cutoffTime);
}
