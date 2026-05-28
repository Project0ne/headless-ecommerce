package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Review entity operations.
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Find reviews by product ID with pagination, ordered by newest first.
     */
    Page<Review> findByProductIdOrderByCreatedAtDesc(Long productId, Pageable pageable);

    /**
     * Check if a user has already reviewed a product.
     */
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    /**
     * Find a specific user's review for a product.
     */
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * Count reviews for a product.
     */
    long countByProductId(Long productId);

    /**
     * Calculate average rating for a product.
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
}
