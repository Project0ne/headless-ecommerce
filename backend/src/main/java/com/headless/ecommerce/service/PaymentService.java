package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.response.OrderResponse;

/**
 * Payment service interface for processing payments.
 */
public interface PaymentService {

    /**
     * Processes a payment for the given order.
     *
     * @param orderNo the order number
     * @return the updated order response after payment
     */
    OrderResponse processPayment(String orderNo);
}
