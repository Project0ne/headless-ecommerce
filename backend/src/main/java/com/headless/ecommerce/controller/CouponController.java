package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.CouponResponse;
import com.headless.ecommerce.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for coupon operations.
 */
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupons", description = "Coupon validation and application")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "Validate a coupon code")
    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<CouponResponse>> validateCoupon(
            @PathVariable String code,
            @RequestParam BigDecimal orderAmount) {
        CouponResponse response = couponService.validateCoupon(code, orderAmount);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "Apply a coupon (increment usage)")
    @PostMapping("/{code}/apply")
    public ResponseEntity<ApiResponse<Void>> applyCoupon(@PathVariable String code) {
        couponService.applyCoupon(code);
        return ResponseEntity.ok(ApiResponse.success(null, "Coupon applied"));
    }
}
