package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Promotion entity operations.
 */
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    /**
     * Find all currently active promotions.
     */
    @Query("SELECT p FROM Promotion p WHERE p.active = true AND p.startDate <= :now AND p.endDate >= :now ORDER BY p.priority DESC")
    List<Promotion> findActivePromotions(@Param("now") LocalDateTime now);

    /**
     * Find active promotions for a specific product.
     */
    @Query("SELECT p FROM Promotion p JOIN ProductPromotion pp ON pp.promotion = p WHERE pp.product.id = :productId AND p.active = true AND p.startDate <= :now AND p.endDate >= :now ORDER BY p.priority DESC")
    List<Promotion> findActivePromotionsByProductId(@Param("productId") Long productId, @Param("now") LocalDateTime now);
}
