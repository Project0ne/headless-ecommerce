import api from "@/lib/api";
import { API_PATHS } from "@/lib/constants";
import type { ApiResponse } from "@/types/api";
import type { Order } from "@/types/order";

/**
 * Processes a payment for an order.
 */
export async function processPayment(
  orderNo: string
): Promise<ApiResponse<Order>> {
  const response = await api.post<ApiResponse<Order>>(
    API_PATHS.PAYMENTS.PROCESS(orderNo)
  );
  return response.data;
}

/**
 * Uploads an image file (admin).
 */
export async function uploadImage(
  file: File
): Promise<ApiResponse<{ url: string }>> {
  const formData = new FormData();
  formData.append("file", file);

  const response = await api.post<ApiResponse<{ url: string }>>(
    API_PATHS.ADMIN.UPLOAD,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );
  return response.data;
}

/**
 * Gets dashboard statistics (admin).
 */
export async function getDashboard(): Promise<
  ApiResponse<{
    totalProducts: number;
    totalOrders: number;
    totalRevenue: number;
    totalUsers: number;
  }>
> {
  const response = await api.get(API_PATHS.ADMIN.DASHBOARD);
  return response.data;
}
