package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductResponse;
import com.headless.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * Product controller for public product browsing.
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product browsing APIs")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Gets a paginated list of products with optional filters.
     *
     * @param page page number (0-based)
     * @param size page size (default 12)
     * @param categoryId optional category filter
     * @param keyword optional search keyword
     * @param sort sort specification (e.g., "price,asc")
     * @return paginated product response
     */
    @GetMapping
    @Operation(summary = "Get product list with pagination and filters")
    public ApiResponse<PageResponse<ProductResponse>> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort) {
        PageResponse<ProductResponse> response = productService
            .getProducts(page, size, categoryId, keyword, sort);
        return ApiResponse.success(response);
    }

    /**
     * Gets a product by ID.
     *
     * @param id the product ID
     * @return the product response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get product detail by ID")
    public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ApiResponse.success(response);
    }

    /**
     * Searches products by keyword.
     *
     * @param keyword the search keyword
     * @param page page number
     * @param size page size
     * @return paginated search results
     */
    @GetMapping("/search")
    @Operation(summary = "Search products by keyword")
    public ApiResponse<PageResponse<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        PageResponse<ProductResponse> response = productService
            .getProducts(page, size, null, keyword, null);
        return ApiResponse.success(response);
    }
}
