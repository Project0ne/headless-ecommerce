package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Coupon entity operations.
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * Find a coupon by its code.
     */
    Optional<Coupon> findByCode(String code);

    /**
     * Check if a coupon code exists.
     */
    boolean existsByCode(String code);
}
