package com.headless.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Summary of product reviews including average rating and distribution.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewSummary {

    private Long productId;
    private Double averageRating;
    private Long totalReviews;
    private Map<Integer, Long> ratingDistribution; // rating -> count
}
