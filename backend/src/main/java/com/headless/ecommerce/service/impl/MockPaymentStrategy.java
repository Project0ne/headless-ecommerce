package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.service.strategy.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Mock payment strategy that always succeeds (for development/testing).
 */
@Component
public class MockPaymentStrategy implements PaymentStrategy {

    /**
     * Simulates a payment by always returning success.
     *
     * @param orderNo the order number
     * @param amount the payment amount
     * @return a successful PaymentResult
     */
    @Override
    public PaymentResult pay(String orderNo, BigDecimal amount) {
        String transactionId = "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return PaymentResult.success(transactionId);
    }
}
