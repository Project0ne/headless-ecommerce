package com.headless.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for product review.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Long userId;
    private String username;
    private Long productId;
    private Integer rating;
    private String title;
    private String content;
    private Boolean isAnonymous;
    private String createdAt;
    private String updatedAt;
}
