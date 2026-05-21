import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useAuthStore } from "@/stores/auth-store";
import {
  register as registerApi,
  login as loginApi,
  getCurrentUser,
  updateCurrentUser,
} from "@/services/auth-service";
import type { LoginRequest, RegisterRequest, UserUpdateRequest } from "@/types/user";
import type { JwtResponse } from "@/types/user";
import type { User } from "@/types/user";
import type { ApiResponse } from "@/types/api";

/**
 * Hook for user registration.
 */
export function useRegister() {
  const setAuth = useAuthStore((state) => state.setAuth);

  return useMutation<ApiResponse<JwtResponse>, Error, RegisterRequest>({
    mutationFn: registerApi,
    onSuccess: (response) => {
      if (response.code === 200 && response.data) {
        setAuth(response.data);
      }
    },
  });
}

/**
 * Hook for user login.
 */
export function useLogin() {
  const setAuth = useAuthStore((state) => state.setAuth);

  return useMutation<ApiResponse<JwtResponse>, Error, LoginRequest>({
    mutationFn: loginApi,
    onSuccess: (response) => {
      if (response.code === 200 && response.data) {
        setAuth(response.data);
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
