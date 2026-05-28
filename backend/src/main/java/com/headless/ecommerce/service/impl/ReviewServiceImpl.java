package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.request.ReviewCreateRequest;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductReviewSummary;
import com.headless.ecommerce.dto.response.ReviewResponse;
import com.headless.ecommerce.exception.BusinessException;
import com.headless.ecommerce.exception.ResourceNotFoundException;
import com.headless.ecommerce.model.Product;
import com.headless.ecommerce.model.Review;
import com.headless.ecommerce.model.User;
import com.headless.ecommerce.repository.ProductRepository;
import com.headless.ecommerce.repository.ReviewRepository;
import com.headless.ecommerce.service.ReviewService;
import com.headless.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of ReviewService.
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ReviewResponse createReview(Long userId, Long productId, ReviewCreateRequest request) {
        // Check if user already reviewed this product
        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new BusinessException("You have already reviewed this product");
        }

        User user = userService.findById(userId);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Review review = Review.builder()
            .user(user)
            .product(product)
            .rating(request.getRating())
            .title(request.getTitle())
            .content(request.getContent())
            .isAnonymous(request.getIsAnonymous())
            .build();

        Review saved = reviewRepository.save(review);
        return toResponse(saved);
    }

    @Override
    public PageResponse<ReviewResponse> getProductReviews(Long productId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId, pageable);

        List<ReviewResponse> content = reviewPage.getContent().stream()
            .map(this::toResponse)
            .toList();

        return PageResponse.of(content, reviewPage.getTotalElements(),
            reviewPage.getTotalPages(), reviewPage.getNumber(), reviewPage.getSize());
    }

    @Override
    public ProductReviewSummary getProductReviewSummary(Long productId) {
        Double avgRating = reviewRepository.getAverageRatingByProductId(productId);
        Long totalReviews = reviewRepository.countByProductId(productId);

        // Build rating distribution (1-5 stars)
        Map<Integer, Long> distribution = new HashMap<>();
        // This would need a custom query for efficiency, simplified for now
        for (int i = 1; i <= 5; i++) {
            distribution.put(i, 0L);
        }

        return ProductReviewSummary.builder()
            .productId(productId)
            .averageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : 0.0)
            .totalReviews(totalReviews)
            .ratingDistribution(distribution)
            .build();
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new ResourceNotFoundException("Review", "id", reviewId));

        // Only the author can delete their review
        if (!review.getUser().getId().equals(userId)) {
            throw new BusinessException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    @Override
    public ReviewResponse getUserReviewForProduct(Long userId, Long productId) {
        return reviewRepository.findByUserIdAndProductId(userId, productId)
            .map(this::toResponse)
            .orElse(null);
    }

    private ReviewResponse toResponse(Review review) {
        return ReviewResponse.builder()
            .id(review.getId())
            .userId(review.getUser().getId())
            .username(review.getIsAnonymous() ? "Anonymous" : review.getUser().getUsername())
            .productId(review.getProduct().getId())
            .rating(review.getRating())
            .title(review.getTitle())
            .content(review.getContent())
            .isAnonymous(review.getIsAnonymous())
            .createdAt(review.getCreatedAt() != null ? review.getCreatedAt().toString() : null)
            .updatedAt(review.getUpdatedAt() != null ? review.getUpdatedAt().toString() : null)
            .build();
    }
}
