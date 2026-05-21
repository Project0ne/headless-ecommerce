package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.request.CategoryCreateRequest;
import com.headless.ecommerce.dto.request.OrderStatusUpdateRequest;
import com.headless.ecommerce.dto.request.ProductCreateRequest;
import com.headless.ecommerce.dto.request.ProductUpdateRequest;
import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.CategoryResponse;
import com.headless.ecommerce.dto.response.OrderResponse;
import com.headless.ecommerce.dto.response.PageResponse;
import com.headless.ecommerce.dto.response.ProductResponse;
import com.headless.ecommerce.service.CategoryService;
import com.headless.ecommerce.service.FileStorageService;
import com.headless.ecommerce.service.OrderService;
import com.headless.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin controller for administrative operations.
 */
@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OrderService orderService;
    private final FileStorageService fileStorageService;

    public AdminController(ProductService productService,
                            CategoryService categoryService,
                            OrderService orderService,
                            FileStorageService fileStorageService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.orderService = orderService;
        this.fileStorageService = fileStorageService;
    }

    // ========== Product Management ==========

    /**
     * Creates a new product.
     */
    @PostMapping("/products")
    @Operation(summary = "Create a new product")
    public ApiResponse<ProductResponse> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ApiResponse.success(response);
    }

    /**
     * Updates an existing product.
     */
    @PutMapping("/products/{id}")
    @Operation(summary = "Update a product")
    public ApiResponse<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ApiResponse.success(response);
    }

    /**
     * Deletes a product (sets to OFF_SHELF).
     */
    @DeleteMapping("/products/{id}")
    @Operation(summary = "Delete a product (soft delete)")
    public ApiResponse<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ApiResponse.success();
    }

    /**
     * Updates a product's status.
     */
    @PutMapping("/products/{id}/status")
    @Operation(summary = "Update product status (on/off shelf)")
    public ApiResponse<ProductResponse> updateProductStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String status = body.get("status");
        ProductResponse response = productService.updateProductStatus(id, status);
        return ApiResponse.success(response);
    }

    // ========== Category Management ==========

    /**
     * Creates a new category.
     */
    @PostMapping("/categories")
    @Operation(summary = "Create a new category")
    public ApiResponse<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryCreateRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ApiResponse.success(response);
    }

    /**
     * Updates an existing category.
     */
    @PutMapping("/categories/{id}")
    @Operation(summary = "Update a category")
    public ApiResponse<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryCreateRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ApiResponse.success(response);
    }

    /**
     * Deletes a category.
     */
    @DeleteMapping("/categories/{id}")
    @Operation(summary = "Delete a category")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success();
    }

    // ========== Order Management ==========

    /**
     * Gets a paginated list of all orders.
     */
    @GetMapping("/orders")
    @Operation(summary = "Get all orders (admin)")
    public ApiResponse<PageResponse<OrderResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        PageResponse<OrderResponse> response = orderService.getAllOrders(page, size);
        return ApiResponse.success(response);
    }

    /**
     * Updates an order's status.
     */
    @PutMapping("/orders/{id}/status")
    @Operation(summary = "Update order status (admin)")
    public ApiResponse<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        OrderResponse response = orderService.updateOrderStatus(id, request.getStatus());
        return ApiResponse.success(response);
    }

    // ========== File Upload ==========

    /**
     * Uploads an image file.
     */
    @PostMapping("/upload")
    @Operation(summary = "Upload an image file")
    public ApiResponse<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file) {
        String fileUrl = fileStorageService.store(file, "images");
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        return ApiResponse.success(result);
    }

    // ========== Dashboard ==========

    /**
     * Gets dashboard statistics.
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get admin dashboard data")
    public ApiResponse<Map<String, Object>> getDashboard() {
        Map<String, Object> dashboard = new HashMap<>();
        // Placeholder dashboard data - in production, calculate from DB
        dashboard.put("totalProducts", 0);
        dashboard.put("totalOrders", 0);
        dashboard.put("totalRevenue", 0);
        dashboard.put("totalUsers", 0);
        return ApiResponse.success(dashboard);
    }
}
