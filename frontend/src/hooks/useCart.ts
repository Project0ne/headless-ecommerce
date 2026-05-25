import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useAuthStore } from "@/stores/auth-store";
import { useCartStore } from "@/stores/cart-store";
import {
  getCart,
  addToCart as addToCartApi,
  updateCartItem as updateCartItemApi,
  removeCartItem as removeCartItemApi,
  mergeCart as mergeCartApi,
  clearCart as clearCartApi,
} from "@/services/cart-service";
import type { CartItemRequest, MergeCartRequest } from "@/types/cart";
import type { CartItemAddRequest } from "@/stores/cart-store";
import type { CartItem } from "@/types/cart";
import type { ApiResponse } from "@/types/api";

/**
 * Hook for fetching the user's cart (authenticated users only).
 */
export function useCart() {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const setItems = useCartStore((state) => state.setItems);

  return useQuery<ApiResponse<CartItem[]>, Error>({
    queryKey: ["cart"],
    queryFn: async () => {
      const response = await getCart();
      if (response.code === 200 && response.data) {
        setItems(response.data);
      }
      return response;
    },
    enabled: isAuthenticated,
  });
}

/**
 * Hook for adding an item to the cart.
 * Accepts optional product details for local cart storage.
 */
export function useAddToCart() {
  const queryClient = useQueryClient();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const addItem = useCartStore((state) => state.addItem);

  return useMutation<ApiResponse<CartItem>, Error, CartItemAddRequest>({
    mutationFn: async (variables) => {
      if (!isAuthenticated) {
        addItem(variables);
        return { code: 200, message: "Added to local cart", data: undefined } as unknown as ApiResponse<CartItem>;
      }
      // Only send productId and quantity to the API
      const { productId, quantity } = variables;
      return addToCartApi({ productId, quantity });
    },
    onSuccess: () => {
      if (isAuthenticated) {
        queryClient.invalidateQueries({ queryKey: ["cart"] });
      }
    },
  });
}

/**
 * Hook for updating a cart item quantity.
 */
export function useUpdateCartItem() {
  const queryClient = useQueryClient();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const updateQuantity = useCartStore((state) => state.updateQuantity);

  return useMutation<
    ApiResponse<CartItem>,
    Error,
    { productId: number; quantity: number }
  >({
    mutationFn: async ({ productId, quantity }) => {
      if (!isAuthenticated) {
        updateQuantity(productId, quantity);
        return { code: 200, message: "Updated local cart", data: undefined } as unknown as ApiResponse<CartItem>;
      }
      return updateCartItemApi(productId, quantity);
    },
    onSuccess: () => {
      if (isAuthenticated) {
        queryClient.invalidateQueries({ queryKey: ["cart"] });
      }
    },
  });
}

/**
 * Hook for removing a cart item.
 */
export function useRemoveCartItem() {
  const queryClient = useQueryClient();
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const removeItem = useCartStore((state) => state.removeItem);

  return useMutation<ApiResponse<void>, Error, number>({
    mutationFn: async (productId) => {
      if (!isAuthenticated) {
        removeItem(productId);
        return { code: 200, message: "Removed from local cart", data: undefined } as unknown as ApiResponse<void>;
      }
      return removeCartItemApi(productId);
    },
    onSuccess: () => {
      if (isAuthenticated) {
        queryClient.invalidateQueries({ queryKey: ["cart"] });
      }
    },
  });
}

/**
 * Hook for merging local cart with server cart on login.
 */
export function useMergeCart() {
  const queryClient = useQueryClient();
  const clearCart = useCartStore((state) => state.clearCart);

  return useMutation<ApiResponse<CartItem[]>, Error, MergeCartRequest>({
    mutationFn: mergeCartApi,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["cart"] });
      clearCart();
    },
  });
}
