package com.headless.ecommerce.repository;

import com.headless.ecommerce.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Category entity operations.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds all top-level categories (no parent) ordered by sortOrder.
     *
     * @return the list of root categories
     */
    List<Category> findByParentIsNullOrderBySortOrderAsc();

    /**
     * Finds child categories by parent ID ordered by sortOrder.
     *
     * @param parentId the parent category ID
     * @return the list of child categories
     */
    List<Category> findByParentIdOrderBySortOrderAsc(Long parentId);
}
