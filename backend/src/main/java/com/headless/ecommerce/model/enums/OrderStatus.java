package com.headless.ecommerce.model.enums;

/**
 * Order status enumeration with state machine transitions.
 */
public enum OrderStatus {
    PENDING_PAYMENT,
    PENDING_SHIPMENT,
    SHIPPING,
    COMPLETED,
    CANCELLED
}
