import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useAuthStore } from "@/stores/auth-store";
import { useCartStore } from "@/stores/cart-store";
import {
  register as registerApi,
  login as loginApi,
  getCurrentUser,
  updateCurrentUser,
} from "@/services/auth-service";
import { mergeCart as mergeCartApi } from "@/services/cart-service";
import type { LoginRequest, RegisterRequest, UserUpdateRequest } from "@/types/user";
import type { JwtResponse } from "@/types/user";
import type { User } from "@/types/user";
import type { ApiResponse } from "@/types/api";

/**
 * Hook for user registration.
 */
export function useRegister() {
  const setAuth = useAuthStore((state) => state.setAuth);
  const localItems = useCartStore((state) => state.items);
  const clearCart = useCartStore((state) => state.clearCart);
  const queryClient = useQueryClient();

  return useMutation<ApiResponse<JwtResponse>, Error, RegisterRequest>({
    mutationFn: registerApi,
    onSuccess: async (response) => {
      if (response.code === 200 && response.data) {
        setAuth(response.data);
        // Merge local cart with server cart after registration
        if (localItems.length > 0) {
          try {
            await mergeCartApi({ items: localItems });
            clearCart();
          } catch {
            // Merge failed silently — server cart will be used
          }
        }
        queryClient.invalidateQueries({ queryKey: ["cart"] });
      }
    },
  });
}

/**
 * Hook for user login.
 */
export function useLogin() {
  const setAuth = useAuthStore((state) => state.setAuth);
  const localItems = useCartStore((state) => state.items);
  const clearCart = useCartStore((state) => state.clearCart);
  const queryClient = useQueryClient();

  return useMutation<ApiResponse<JwtResponse>, Error, LoginRequest>({
    mutationFn: loginApi,
    onSuccess: async (response) => {
      if (response.code === 200 && response.data) {
        setAuth(response.data);
        // Merge local cart with server cart after login
        if (localItems.length > 0) {
          try {
            await mergeCartApi({ items: localItems });
            clearCart();
          } catch {
            // Merge failed silently — server cart will be used
          }
        }
        queryClient.invalidateQueries({ queryKey: ["cart"] });
      }
    },
  });
}

/**
 * Hook for fetching the current user profile.
 */
export function useCurrentUser() {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated);
  const setUser = useAuthStore((state) => state.setUser);

  return useQuery<ApiResponse<User>, Error>({
    queryKey: ["currentUser"],
    queryFn: async () => {
      const response = await getCurrentUser();
      if (response.code === 200 && response.data) {
        setUser(response.data);
      }
      return response;
    },
    enabled: isAuthenticated,
  });
}

/**
 * Hook for updating the current user profile.
 */
export function useUpdateUser() {
  const queryClient = useQueryClient();

  return useMutation<ApiResponse<User>, Error, UserUpdateRequest>({
    mutationFn: updateCurrentUser,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["currentUser"] });
    },
  });
}
