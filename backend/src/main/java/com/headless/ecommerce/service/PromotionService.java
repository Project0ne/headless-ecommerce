package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.response.PromotionResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for promotion management.
 */
public interface PromotionService {

    /**
     * Get all active promotions for a product.
     */
    List<PromotionResponse> getProductPromotions(Long productId);

    /**
     * Get all active site-wide promotions.
     */
    List<PromotionResponse> getActivePromotions();

    /**
     * Calculate the best promotion discount for given products and total.
     */
    BigDecimal calculatePromotionDiscount(Long productId, BigDecimal originalPrice, int quantity);

    /**
     * Get promotional price for a product (returns original if no promotion).
     */
    BigDecimal getPromotionalPrice(Long productId, BigDecimal originalPrice);
}
