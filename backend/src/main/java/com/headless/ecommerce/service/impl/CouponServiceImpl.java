package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.response.CouponResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.model.Coupon;
import com.headless.ecommerce.repository.CouponRepository;
import com.headless.ecommerce.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Implementation of CouponService.
 */
@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public CouponResponse validateCoupon(String code, BigDecimal orderAmount) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
            .orElseThrow(() -> new BusinessException("Invalid coupon code"));

        if (!coupon.isValid()) {
            return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .valid(false)
                .message("Coupon is expired or usage limit reached")
                .build();
        }

        if (orderAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            return CouponResponse.builder()
                .id(coupon.getId())
                .code(coupon.getCode())
                .valid(false)
                .message("Order amount does not meet minimum requirement of " + coupon.getMinOrderAmount())
                .build();
        }

        BigDecimal discount = coupon.calculateDiscount(orderAmount);

        return CouponResponse.builder()
            .id(coupon.getId())
            .code(coupon.getCode())
            .type(coupon.getType().name())
            .value(coupon.getValue())
            .minOrderAmount(coupon.getMinOrderAmount())
            .maxDiscount(coupon.getMaxDiscount())
            .discountAmount(discount)
            .valid(true)
            .message("Coupon applied successfully")
            .build();
    }

    @Override
    @Transactional
    public void applyCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
            .orElseThrow(() -> new BusinessException("Invalid coupon code"));

        if (!coupon.isValid()) {
            throw new BusinessException("Coupon is expired or usage limit reached");
        }

        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);
    }
}
