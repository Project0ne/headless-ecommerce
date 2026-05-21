import api from "@/lib/api";
import { API_PATHS } from "@/lib/constants";
import type { ApiResponse } from "@/types/api";
import type { LoginRequest, RegisterRequest, User, UserUpdateRequest, JwtResponse } from "@/types/user";

/**
 * Registers a new user.
 */
export async function register(
  data: RegisterRequest
): Promise<ApiResponse<JwtResponse>> {
  const response = await api.post<ApiResponse<JwtResponse>>(
    API_PATHS.AUTH.REGISTER,
    data
  );
  return response.data;
}

/**
 * Logs in a user.
 */
export async function login(
  data: LoginRequest
): Promise<ApiResponse<JwtResponse>> {
  const response = await api.post<ApiResponse<JwtResponse>>(
    API_PATHS.AUTH.LOGIN,
    data
  );
  return response.data;
}

/**
 * Gets the current user profile.
 */
export async function getCurrentUser(): Promise<ApiResponse<User>> {
  const response = await api.get<ApiResponse<User>>(API_PATHS.USER.ME);
  return response.data;
}

/**
 * Updates the current user profile.
 */
export async function updateCurrentUser(
  data: UserUpdateRequest
): Promise<ApiResponse<User>> {
  const response = await api.put<ApiResponse<User>>(
    API_PATHS.USER.ME,
    data
  );
  return response.data;
}
