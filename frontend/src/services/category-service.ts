import api from "@/lib/api";
import { API_PATHS } from "@/lib/constants";
import type { ApiResponse } from "@/types/api";
import type { Category, CategoryCreateRequest } from "@/types/category";

/**
 * Gets the category tree.
 */
export async function getCategories(): Promise<ApiResponse<Category[]>> {
  const response = await api.get<ApiResponse<Category[]>>(
    API_PATHS.CATEGORIES.LIST
  );
  return response.data;
}

/**
 * Creates a new category (admin).
 */
export async function createCategory(
  data: CategoryCreateRequest
): Promise<ApiResponse<Category>> {
  const response = await api.post<ApiResponse<Category>>(
    API_PATHS.ADMIN.CATEGORIES,
    data
  );
  return response.data;
}

/**
 * Updates a category (admin).
 */
export async function updateCategory(
  id: number,
  data: CategoryCreateRequest
): Promise<ApiResponse<Category>> {
  const response = await api.put<ApiResponse<Category>>(
    API_PATHS.ADMIN.CATEGORY_DETAIL(id),
    data
  );
  return response.data;
}

/**
 * Deletes a category (admin).
 */
export async function deleteCategory(id: number): Promise<ApiResponse<void>> {
  const response = await api.delete<ApiResponse<void>>(
    API_PATHS.ADMIN.CATEGORY_DETAIL(id)
  );
  return response.data;
}
