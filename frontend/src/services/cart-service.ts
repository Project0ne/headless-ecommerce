import api from "@/lib/api";
import { API_PATHS } from "@/lib/constants";
import type { ApiResponse } from "@/types/api";
import type { CartItem, CartItemRequest, MergeCartRequest } from "@/types/cart";

/**
 * Gets the user's cart.
 */
export async function getCart(): Promise<ApiResponse<CartItem[]>> {
  const response = await api.get<ApiResponse<CartItem[]>>(API_PATHS.CART.GET);
  return response.data;
}

/**
 * Adds an item to the cart.
 */
export async function addToCart(
  data: CartItemRequest
): Promise<ApiResponse<CartItem>> {
  const response = await api.post<ApiResponse<CartItem>>(
    API_PATHS.CART.ADD_ITEM,
    data
  );
  return response.data;
}

/**
 * Updates a cart item's quantity.
 */
export async function updateCartItem(
  productId: number,
  quantity: number
): Promise<ApiResponse<CartItem>> {
  const response = await api.put<ApiResponse<CartItem>>(
    API_PATHS.CART.UPDATE_ITEM(productId),
    null,
    { params: { quantity } }
  );
  return response.data;
}

/**
 * Removes a cart item.
 */
export async function removeCartItem(
  productId: number
): Promise<ApiResponse<void>> {
  const response = await api.delete<ApiResponse<void>>(
    API_PATHS.CART.REMOVE_ITEM(productId)
  );
  return response.data;
}

/**
 * Merges local cart items with the server cart.
 */
export async function mergeCart(
  data: MergeCartRequest
): Promise<ApiResponse<CartItem[]>> {
  const response = await api.post<ApiResponse<CartItem[]>>(
    API_PATHS.CART.MERGE,
    data.items
  );
  return response.data;
}

/**
 * Clears the user's cart (not directly exposed via API; handled server-side on order creation).
 */
export async function clearCart(): Promise<void> {
  // Cart is cleared by the server when an order is created
}
