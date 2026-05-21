package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.request.CartItemRequest;
import com.headless.ecommerce.dto.request.OrderCreateRequest;
import com.headless.ecommerce.dto.response.CartItemResponse;

import java.util.List;

/**
 * Cart service interface for shopping cart operations.
 */
public interface CartService {

    /**
     * Gets the user's cart items.
     *
     * @param userId the user ID
     * @return the list of cart item responses
     */
    List<CartItemResponse> getCart(Long userId);

    /**
     * Adds an item to the user's cart.
     *
     * @param userId the user ID
     * @param request the cart item request
     * @return the added cart item response
     */
    CartItemResponse addToCart(Long userId, CartItemRequest request);

    /**
     * Updates the quantity of a cart item.
     *
     * @param userId the user ID
     * @param productId the product ID
     * @param quantity the new quantity
     * @return the updated cart item response
     */
    CartItemResponse updateQuantity(Long userId, Long productId, Integer quantity);

    /**
     * Removes an item from the user's cart.
     *
     * @param userId the user ID
     * @param productId the product ID
     */
    void removeItem(Long userId, Long productId);

    /**
     * Merges local cart items with the user's server cart.
     *
     * @param userId the user ID
     * @param localItems the local cart items to merge
     * @return the merged cart items
     */
    List<CartItemResponse> mergeCart(Long userId, List<CartItemRequest> localItems);

    /**
     * Clears the user's cart.
     *
     * @param userId the user ID
     */
    void clearCart(Long userId);
}
