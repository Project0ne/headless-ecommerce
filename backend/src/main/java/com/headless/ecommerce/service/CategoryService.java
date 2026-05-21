package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.CategoryCreateRequest;
import com.headless.ecommerce.dto.response.CategoryResponse;

import java.util.List;

/**
 * Category service interface for category management.
 */
public interface CategoryService {

    /**
     * Gets the category tree.
     *
     * @return the list of root categories with children
     */
    List<CategoryResponse> getCategoryTree();

    /**
     * Creates a new category.
     *
     * @param request the category creation request
     * @return the created category response
     */
    CategoryResponse createCategory(CategoryCreateRequest request);

    /**
     * Updates an existing category.
     *
     * @param id the category ID
     * @param request the category update request
     * @return the updated category response
     */
    CategoryResponse updateCategory(Long id, CategoryCreateRequest request);

    /**
     * Deletes a category.
     *
     * @param id the category ID
     */
    void deleteCategory(Long id);
}
