package com.headless.ecommerce.controller;

import com.headless.ecommerce.dto.request.CartItemRequest;
import com.headless.ecommerce.dto.response.ApiResponse;
import com.headless.ecommerce.dto.response.CartItemResponse;
import com.headless.ecommerce.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cart controller for shopping cart operations.
 */
@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart", description = "Shopping cart APIs")
public class CartController {

    private final CartService cartService;
    private final com.headless.ecommerce.service.UserService userService;

    public CartController(CartService cartService,
                           com.headless.ecommerce.service.UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    /**
     * Gets the current user's cart.
     *
     * @param userDetails the authenticated user
     * @return the list of cart items
     */
    @GetMapping
    @Operation(summary = "Get current user's cart")
    public ApiResponse<List<CartItemResponse>> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        Long userId = getUserId(userDetails);
        List<CartItemResponse> response = cartService.getCart(userId);
        return ApiResponse.success(response);
    }

    /**
     * Adds an item to the cart.
     *
     * @param userDetails the authenticated user
     * @param request the cart item request
     * @return the added cart item
     */
    @PostMapping("/items")
    @Operation(summary = "Add item to cart")
    public ApiResponse<CartItemResponse> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CartItemRequest request) {
        Long userId = getUserId(userDetails);
        CartItemResponse response = cartService.addToCart(userId, request);
        return ApiResponse.success(response);
    }

    /**
     * Updates the quantity of a cart item.
     *
     * @param userDetails the authenticated user
     * @param productId the product ID
     * @param quantity the new quantity
     * @return the updated cart item
     */
    @PutMapping("/items/{productId}")
    @Operation(summary = "Update cart item quantity")
    public ApiResponse<CartItemResponse> updateCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        Long userId = getUserId(userDetails);
        CartItemResponse response = cartService.updateQuantity(userId, productId, quantity);
        return ApiResponse.success(response);
    }

    /**
     * Removes an item from the cart.
     *
     * @param userDetails the authenticated user
     * @param productId the product ID
     * @return success response
     */
    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Remove item from cart")
    public ApiResponse<Void> removeCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        Long userId = getUserId(userDetails);
        cartService.removeItem(userId, productId);
        return ApiResponse.success();
    }

    /**
     * Merges local cart items with server cart.
     *
     * @param userDetails the authenticated user
     * @param localItems the local cart items to merge
     * @return the merged cart items
     */
    @PostMapping("/merge")
    @Operation(summary = "Merge local cart with server cart")
    public ApiResponse<List<CartItemResponse>> mergeCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody List<@Valid CartItemRequest> localItems) {
        Long userId = getUserId(userDetails);
        List<CartItemResponse> response = cartService.mergeCart(userId, localItems);
        return ApiResponse.success(response);
    }

    /**
     * Extracts the user ID from authenticated user details.
     */
    private Long getUserId(UserDetails userDetails) {
        return userService.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new com.headless.ecommerce.exception.ResourceNotFoundException(
                "User", "username", userDetails.getUsername()))
            .getId();
    }
}
