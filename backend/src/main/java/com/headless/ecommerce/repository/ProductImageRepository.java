package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ProductImage entity operations.
 */
@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    /**
     * Find all images for a product, ordered by sort order.
     */
    List<ProductImage> findByProductIdOrderBySortOrderAsc(Long productId);

    /**
     * Find the primary image for a product.
     */
    ProductImage findByProductIdAndIsPrimaryTrue(Long productId);
}
