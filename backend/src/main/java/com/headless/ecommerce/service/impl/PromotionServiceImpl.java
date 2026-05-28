package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.response.PromotionResponse;
import com.headless.ecommerce.model.Promotion;
import com.headless.ecommerce.model.enums.PromotionType;
import com.headless.ecommerce.repository.PromotionRepository;
import com.headless.ecommerce.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of PromotionService.
 */
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public List<PromotionResponse> getProductPromotions(Long productId) {
        List<Promotion> promotions = promotionRepository.findActivePromotionsByProductId(
            productId, LocalDateTime.now());
        return promotions.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<PromotionResponse> getActivePromotions() {
        List<Promotion> promotions = promotionRepository.findActivePromotions(LocalDateTime.now());
        return promotions.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculatePromotionDiscount(Long productId, BigDecimal originalPrice, int quantity) {
        List<Promotion> promotions = promotionRepository.findActivePromotionsByProductId(
            productId, LocalDateTime.now());

        BigDecimal bestDiscount = BigDecimal.ZERO;

        for (Promotion promotion : promotions) {
            BigDecimal discount = calculateDiscountForPromotion(promotion, originalPrice, quantity);
            if (discount.compareTo(bestDiscount) > 0) {
                bestDiscount = discount;
            }
        }

        return bestDiscount;
    }

    @Override
    public BigDecimal getPromotionalPrice(Long productId, BigDecimal originalPrice) {
        List<Promotion> promotions = promotionRepository.findActivePromotionsByProductId(
            productId, LocalDateTime.now());

        if (promotions.isEmpty()) {
            return originalPrice;
        }

        // Get the best discount
        BigDecimal bestDiscount = BigDecimal.ZERO;
        for (Promotion promotion : promotions) {
            BigDecimal discount = calculateDiscountForPromotion(promotion, originalPrice, 1);
            if (discount.compareTo(bestDiscount) > 0) {
                bestDiscount = discount;
            }
        }

        return originalPrice.subtract(bestDiscount).max(BigDecimal.ZERO);
    }

    private BigDecimal calculateDiscountForPromotion(Promotion promotion, BigDecimal price, int quantity) {
        if (promotion.getDiscountType() == null || promotion.getDiscountValue() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalAmount = price.multiply(BigDecimal.valueOf(quantity));

        if (promotion.getMinPurchaseAmount() != null &&
            totalAmount.compareTo(promotion.getMinPurchaseAmount()) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if ("PERCENTAGE".equals(promotion.getDiscountType())) {
            discount = totalAmount.multiply(promotion.getDiscountValue())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discount = promotion.getDiscountValue();
        }

        if (promotion.getMaxDiscountAmount() != null &&
            discount.compareTo(promotion.getMaxDiscountAmount()) > 0) {
            discount = promotion.getMaxDiscountAmount();
        }

        return discount.min(totalAmount);
    }

    private PromotionResponse toResponse(Promotion promotion) {
        return PromotionResponse.builder()
            .id(promotion.getId())
            .name(promotion.getName())
            .description(promotion.getDescription())
            .type(promotion.getType().name())
            .discountType(promotion.getDiscountType())
            .discountValue(promotion.getDiscountValue())
            .minPurchaseAmount(promotion.getMinPurchaseAmount())
            .maxDiscountAmount(promotion.getMaxDiscountAmount())
            .buyQuantity(promotion.getBuyQuantity())
            .getQuantity(promotion.getGetQuantity())
            .startDate(promotion.getStartDate().toString())
            .endDate(promotion.getEndDate().toString())
            .isActive(promotion.isActive())
            .build();
    }
}
