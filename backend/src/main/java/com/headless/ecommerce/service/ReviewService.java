package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.ReviewCreateRequest;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductReviewSummary;
import com.headless.ecommerce.dto.response.ReviewResponse;

/**
 * Service interface for review management.
 */
public interface ReviewService {

    /**
     * Create a new review for a product.
     */
    ReviewResponse createReview(Long userId, Long productId, ReviewCreateRequest request);

    /**
     * Get paginated reviews for a product.
     */
    PageResponse<ReviewResponse> getProductReviews(Long productId, int page, int size);

    /**
     * Get review summary for a product (avg rating, distribution).
     */
    ProductReviewSummary getProductReviewSummary(Long productId);

    /**
     * Delete a review (owner or admin).
     */
    void deleteReview(Long reviewId, Long userId);

    /**
     * Get a user's review for a product.
     */
    ReviewResponse getUserReviewForProduct(Long userId, Long productId);
}
