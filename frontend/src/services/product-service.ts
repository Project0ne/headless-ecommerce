import api from "@/lib/api";
import { API_PATHS } from "@/lib/constants";
import type { ApiResponse, PageResponse } from "@/types/api";
import type { Product, ProductFilters, ProductCreateRequest, ProductUpdateRequest } from "@/types/product";

/**
 * Gets a paginated list of products.
 */
export async function getProducts(
  filters?: ProductFilters
): Promise<ApiResponse<PageResponse<Product>>> {
  const params: Record<string, string | number> = {};
  if (filters?.page !== undefined) params.page = filters.page;
  if (filters?.size !== undefined) params.size = filters.size;
  if (filters?.categoryId !== undefined) params.categoryId = filters.categoryId;
  if (filters?.keyword) params.keyword = filters.keyword;
  if (filters?.sort) params.sort = filters.sort;

  const response = await api.get<ApiResponse<PageResponse<Product>>>(
    API_PATHS.PRODUCTS.LIST,
    { params }
  );
  return response.data;
}

/**
 * Gets a product by ID.
 */
export async function getProductById(id: number): Promise<ApiResponse<Product>> {
  const response = await api.get<ApiResponse<Product>>(
    API_PATHS.PRODUCTS.DETAIL(id)
  );
  return response.data;
}

/**
 * Searches products by keyword.
 */
export async function searchProducts(
  keyword: string,
  page = 0,
  size = 12
): Promise<ApiResponse<PageResponse<Product>>> {
  const response = await api.get<ApiResponse<PageResponse<Product>>>(
    API_PATHS.PRODUCTS.SEARCH,
    { params: { keyword, page, size } }
  );
  return response.data;
}

/**
 * Creates a new product (admin).
 */
export async function createProduct(
  data: ProductCreateRequest
): Promise<ApiResponse<Product>> {
  const response = await api.post<ApiResponse<Product>>(
    API_PATHS.ADMIN.PRODUCTS,
    data
  );
  return response.data;
}

/**
 * Updates a product (admin).
 */
export async function updateProduct(
  id: number,
  data: ProductUpdateRequest
): Promise<ApiResponse<Product>> {
  const response = await api.put<ApiResponse<Product>>(
    API_PATHS.ADMIN.PRODUCT_DETAIL(id),
    data
  );
  return response.data;
}

/**
 * Deletes a product (admin).
 */
export async function deleteProduct(id: number): Promise<ApiResponse<void>> {
  const response = await api.delete<ApiResponse<void>>(
    API_PATHS.ADMIN.PRODUCT_DETAIL(id)
  );
  return response.data;
}

/**
 * Updates a product's status (admin).
 */
export async function updateProductStatus(
  id: number,
  status: "ON_SHELF" | "OFF_SHELF"
): Promise<ApiResponse<Product>> {
  const response = await api.put<ApiResponse<Product>>(
    API_PATHS.ADMIN.PRODUCT_STATUS(id),
    { status }
  );
  return response.data;
}
