package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Product;
import com.headless.ecommerce.model.enums.ProductStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Product entity operations with specification support.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    /**
     * Finds products by category ID and status with pagination.
     *
     * @param categoryId the category ID
     * @param status the product status
     * @param pageable the pagination info
     * @return a page of products
     */
    Page<Product> findByCategoryIdAndStatus(Long categoryId, ProductStatus status, Pageable pageable);

    /**
     * Finds products by status with pagination.
     *
     * @param status the product status
     * @param pageable the pagination info
     * @return a page of products
     */
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    /**
     * Finds all products by ID list with pessimistic lock for stock deduction.
     *
     * @param ids the product IDs
     * @return the list of locked products
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Product> findAllByIdIn(List<Long> ids);
}
