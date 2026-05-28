package com.headless.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Response DTO for promotion.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponse {

    private Long id;
    private String name;
    private String description;
    private String type;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minPurchaseAmount;
    private BigDecimal maxDiscountAmount;
    private Integer buyQuantity;
    private Integer getQuantity;
    private String startDate;
    private String endDate;
    private Boolean isActive;
}
