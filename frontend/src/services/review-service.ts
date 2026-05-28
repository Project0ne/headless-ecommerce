import api from "@/lib/api";
import type { ApiResponse, PageResponse } from "@/types/api";

export interface Review {
  id: number;
  userId: number;
  username: string;
  productId: number;
  rating: number;
  title: string;
  content: string;
  isAnonymous: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ReviewCreateRequest {
  rating: number;
  title?: string;
  content?: string;
  isAnonymous?: boolean;
}

export interface ReviewSummary {
  productId: number;
  averageRating: number;
  totalReviews: number;
  ratingDistribution: Record<number, number>;
}

/**
 * Create a review for a product.
 */
export async function createReview(
  productId: number,
  data: ReviewCreateRequest
): Promise<ApiResponse<Review>> {
  const response = await api.post(`/products/${productId}/reviews`, data);
  return response.data;
}

/**
 * Get reviews for a product.
 */
export async function getProductReviews(
  productId: number,
  page = 0,
  size = 10
): Promise<ApiResponse<PageResponse<Review>>> {
  const response = await api.get(`/products/${productId}/reviews`, {
    params: { page, size },
  });
  return response.data;
}

/**
 * Get review summary for a product.
 */
export async function getReviewSummary(
  productId: number
): Promise<ApiResponse<ReviewSummary>> {
  const response = await api.get(`/products/${productId}/reviews/summary`);
  return response.data;
}

/**
 * Get current user's review for a product.
 */
export async function getMyReview(
  productId: number
): Promise<ApiResponse<Review | null>> {
  const response = await api.get(`/products/${productId}/reviews/my-review`);
  return response.data;
}

/**
 * Delete a review.
 */
export async function deleteReview(
  productId: number,
  reviewId: number
): Promise<ApiResponse<void>> {
  const response = await api.delete(`/products/${productId}/reviews/${reviewId}`);
  return response.data;
}
