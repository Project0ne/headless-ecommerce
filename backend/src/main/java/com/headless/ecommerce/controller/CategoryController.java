package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.CategoryResponse;
import com.headless.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Category controller for public category browsing.
 */
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Categories", description = "Category browsing APIs")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Gets the category tree.
     *
     * @return the list of root categories with nested children
     */
    @GetMapping
    @Operation(summary = "Get category tree")
    public ApiResponse<List<CategoryResponse>> getCategoryTree() {
        List<CategoryResponse> response = categoryService.getCategoryTree();
        return ApiResponse.success(response);
    }
}
