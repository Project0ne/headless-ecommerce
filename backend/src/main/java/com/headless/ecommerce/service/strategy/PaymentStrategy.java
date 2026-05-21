package com.headless.ecommerce.service.strategy;

import java.math.BigDecimal;

/**
 * Payment strategy interface for implementing different payment methods.
 */
public interface PaymentStrategy {

    /**
     * Processes a payment.
     *
     * @param orderNo the order number
     * @param amount the payment amount
     * @return the payment result
     */
    PaymentResult pay(String orderNo, BigDecimal amount);

    /**
     * Payment result record.
     */
    record PaymentResult(Boolean success, String transactionId, String message) {
        /**
         * Creates a successful payment result.
         *
         * @param transactionId the transaction ID
         * @return the successful PaymentResult
         */
        public static PaymentResult success(String transactionId) {
            return new PaymentResult(true, transactionId, "Payment successful");
        }

        /**
         * Creates a failed payment result.
         *
         * @param message the failure message
         * @return the failed PaymentResult
         */
        public static PaymentResult failure(String message) {
            return new PaymentResult(false, null, message);
        }
    }
}
