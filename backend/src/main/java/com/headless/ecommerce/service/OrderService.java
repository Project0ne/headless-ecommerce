package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.OrderCreateRequest;
import com.headless.ecommerce.dto.response.OrderResponse;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.model.Order;
import com.headless.ecommerce.model.enums.OrderStatus;

/**
 * Order service interface for order management.
 */
public interface OrderService {

    /**
     * Creates a new order from the user's cart.
     *
     * @param userId the user ID
     * @param request the order creation request
     * @return the created order response
     */
    OrderResponse createOrder(Long userId, OrderCreateRequest request);

    /**
     * Gets a paginated list of orders for a user.
     *
     * @param userId the user ID
     * @param page page number (0-based)
     * @param size page size
     * @return paginated order response
     */
    PageResponse<OrderResponse> getUserOrders(Long userId, int page, int size);

    /**
     * Gets an order by ID.
     *
     * @param orderId the order ID
     * @param userId the user ID (for ownership verification)
     * @return the order response
     */
    OrderResponse getOrderById(Long orderId, Long userId);

    /**
     * Cancels an order.
     *
     * @param orderId the order ID
     * @param userId the user ID
     * @return the cancelled order response
     */
    OrderResponse cancelOrder(Long orderId, Long userId);

    /**
     * Updates the status of an order (admin operation).
     *
     * @param orderId the order ID
     * @param newStatus the new order status
     * @return the updated order response
     */
    OrderResponse updateOrderStatus(Long orderId, OrderStatus newStatus);

    /**
     * Gets a paginated list of all orders (admin).
     *
     * @param page page number (0-based)
     * @param size page size
     * @return paginated order response
     */
    PageResponse<OrderResponse> getAllOrders(int page, int size);

    /**
     * Restores stock for all items in a cancelled order.
     * Used by OrderTimeoutScheduler to avoid code duplication.
     *
     * @param order the cancelled order
     */
    void restoreStock(Order order);
}
