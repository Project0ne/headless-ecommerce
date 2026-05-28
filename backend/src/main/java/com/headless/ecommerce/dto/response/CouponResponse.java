package com.headless.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for coupon validation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponResponse {

    private Long id;
    private String code;
    private String type;
    private BigDecimal value;
    private BigDecimal minOrderAmount;
    private BigDecimal maxDiscount;
    private BigDecimal discountAmount;
    private boolean valid;
    private String message;
}
