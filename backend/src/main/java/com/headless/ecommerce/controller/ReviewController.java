package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.request.ReviewCreateRequest;
import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductReviewSummary;
import com.headless.ecommerce.dto.response.ReviewResponse;
import com.headless.ecommerce.security.JwtTokenProvider;
import com.headless.ecommerce.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for product review operations.
 */
@RestController
@RequestMapping("/api/v1/products/{productId}/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Product review operations")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Create a review for a product")
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody ReviewCreateRequest request) {
        Long userId = getUserIdFromToken(authorization);
        ReviewResponse review = reviewService.createReview(userId, productId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(review, "Review created successfully"));
    }

    @Operation(summary = "Get reviews for a product")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponse>>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageResponse<ReviewResponse> reviews = reviewService.getProductReviews(productId, page, size);
        return ResponseEntity.ok(ApiResponse.success(reviews));
    }

    @Operation(summary = "Get review summary for a product")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ProductReviewSummary>> getReviewSummary(
            @PathVariable Long productId) {
        ProductReviewSummary summary = reviewService.getProductReviewSummary(productId);
        return ResponseEntity.ok(ApiResponse.success(summary));
    }

    @Operation(summary = "Get current user's review for a product")
    @GetMapping("/my-review")
    public ResponseEntity<ApiResponse<ReviewResponse>> getMyReview(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authorization) {
        Long userId = getUserIdFromToken(authorization);
        ReviewResponse review = reviewService.getUserReviewForProduct(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(review));
    }

    @Operation(summary = "Delete a review")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long productId,
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String authorization) {
        Long userId = getUserIdFromToken(authorization);
        reviewService.deleteReview(reviewId, userId);
        return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
    }

    private Long getUserIdFromToken(String authorization) {
        String token = authorization.replace("Bearer ", "");
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
