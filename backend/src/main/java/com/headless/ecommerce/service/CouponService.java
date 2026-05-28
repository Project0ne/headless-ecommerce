package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.response.CouponResponse;

import java.math.BigDecimal;

/**
 * Service interface for coupon management.
 */
public interface CouponService {

    /**
     * Validate and get coupon details.
     */
    CouponResponse validateCoupon(String code, BigDecimal orderAmount);

    /**
     * Apply a coupon (increment usage count).
     */
    void applyCoupon(String code);
}
