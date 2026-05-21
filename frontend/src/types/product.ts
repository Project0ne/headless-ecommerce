/** Product type definitions matching backend DTOs */

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  stock: number;
  imageUrl: string;
  status: "ON_SHELF" | "OFF_SHELF";
  salesCount: number;
  categoryId: number;
  categoryName: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProductCreateRequest {
  name: string;
  description?: string;
  price: number;
  stock: number;
  imageUrl?: string;
  categoryId: number;
}

export interface ProductUpdateRequest {
  name?: string;
  description?: string;
  price?: number;
  stock?: number;
  imageUrl?: string;
  categoryId?: number;
}

export interface ProductStatusUpdateRequest {
  status: "ON_SHELF" | "OFF_SHELF";
}

export interface ProductFilters {
  keyword?: string;
  categoryId?: number;
  sort?: string;
  page?: number;
  size?: number;
}
