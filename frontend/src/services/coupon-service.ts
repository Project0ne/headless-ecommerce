import api from "@/lib/api";
import type { ApiResponse } from "@/types/api";

export interface Coupon {
  id: number;
  code: string;
  type: string;
  value: number;
  minOrderAmount: number;
  maxDiscount: number | null;
  discountAmount: number;
  valid: boolean;
  message: string;
}

/**
 * Validate a coupon code.
 */
export async function validateCoupon(
  code: string,
  orderAmount: number
): Promise<ApiResponse<Coupon>> {
  const response = await api.get(`/coupons/${code}`, {
    params: { orderAmount },
  });
  return response.data;
}

/**
 * Apply a coupon (increment usage count).
 */
export async function applyCoupon(code: string): Promise<ApiResponse<void>> {
  const response = await api.post(`/coupons/${code}/apply`);
  return response.data;
}
