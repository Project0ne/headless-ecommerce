package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.ProductCreateRequest;
import com.headless.ecommerce.dto.request.ProductUpdateRequest;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductResponse;
import com.headless.ecommerce.model.Product;

/**
 * Product service interface for product management.
 */
public interface ProductService {

    /**
     * Gets a paginated list of on-shelf products with optional filters.
     *
     * @param page page number (0-based)
     * @param size page size
     * @param categoryId optional category filter
     * @param keyword optional search keyword
     * @param sort sort specification
     * @return paginated product response
     */
    PageResponse<ProductResponse> getProducts(int page, int size,
                                               Long categoryId, String keyword, String sort);

    /**
     * Gets a product by ID.
     *
     * @param id the product ID
     * @return the product response
     */
    ProductResponse getProductById(Long id);

    /**
     * Creates a new product.
     *
     * @param request the product creation request
     * @return the created product response
     */
    ProductResponse createProduct(ProductCreateRequest request);

    /**
     * Updates an existing product.
     *
     * @param id the product ID
     * @param request the product update request
     * @return the updated product response
     */
    ProductResponse updateProduct(Long id, ProductUpdateRequest request);

    /**
     * Deletes a product (sets status to OFF_SHELF).
     *
     * @param id the product ID
     */
    void deleteProduct(Long id);

    /**
     * Updates a product's status.
     *
     * @param id the product ID
     * @param status the new status
     * @return the updated product response
     */
    ProductResponse updateProductStatus(Long id, String status);

    /**
     * Finds a Product entity by ID.
     *
     * @param id the product ID
     * @return the Product entity
     */
    Product findById(Long id);
}
