import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  getProducts,
  getProductById,
  searchProducts,
  createProduct,
  updateProduct,
  deleteProduct,
  updateProductStatus,
} from "@/services/product-service";
import type {
  ProductFilters,
  ProductCreateRequest,
  ProductUpdateRequest,
} from "@/types/product";
import type { PageResponse } from "@/types/api";
import type { Product } from "@/types/product";

/**
 * Hook for fetching products with filters.
 */
export function useProducts(filters?: ProductFilters) {
  return useQuery({
    queryKey: ["products", filters],
    queryFn: () => getProducts(filters),
    select: (response) => response.data,
  });
}

/**
 * Hook for fetching a single product by ID.
 */
export function useProduct(id: number) {
  return useQuery({
    queryKey: ["product", id],
    queryFn: () => getProductById(id),
    select: (response) => response.data,
    enabled: !!id,
  });
}

/**
 * Hook for searching products.
 */
export function useProductSearch(keyword: string, page = 0, size = 12) {
  return useQuery({
    queryKey: ["productSearch", keyword, page, size],
    queryFn: () => searchProducts(keyword, page, size),
    select: (response) => response.data,
    enabled: !!keyword,
  });
}

/**
 * Hook for creating a product (admin).
 */
export function useCreateProduct() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createProduct,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["products"] });
    },
  });
}

/**
 * Hook for updating a product (admin).
 */
export function useUpdateProduct() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: ProductUpdateRequest }) =>
      updateProduct(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["products"] });
    },
  });
}

/**
 * Hook for deleting a product (admin).
 */
export function useDeleteProduct() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteProduct,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["products"] });
    },
  });
}

/**
 * Hook for updating a product's status (admin).
 */
export function useUpdateProductStatus() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      id,
      status,
    }: {
      id: number;
      status: "ON_SHELF" | "OFF_SHELF";
    }) => updateProductStatus(id, status),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["products"] });
    },
  });
}
