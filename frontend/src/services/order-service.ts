import api from "@/lib/api";
import { API_PATHS } from "@/lib/constants";
import type { ApiResponse, PageResponse, PageParams } from "@/types/api";
import type {
  Order,
  OrderCreateRequest,
  OrderStatusUpdateRequest,
} from "@/types/order";

/**
 * Creates a new order.
 */
export async function createOrder(
  data: OrderCreateRequest
): Promise<ApiResponse<Order>> {
  const response = await api.post<ApiResponse<Order>>(
    API_PATHS.ORDERS.CREATE,
    data
  );
  return response.data;
}

/**
 * Gets the user's order list.
 */
export async function getUserOrders(
  page = 0,
  size = 12
): Promise<ApiResponse<PageResponse<Order>>> {
  const response = await api.get<ApiResponse<PageResponse<Order>>>(
    API_PATHS.ORDERS.LIST,
    { params: { page, size } }
  );
  return response.data;
}

/**
 * Gets an order by ID.
 */
export async function getOrderById(id: number): Promise<ApiResponse<Order>> {
  const response = await api.get<ApiResponse<Order>>(
    API_PATHS.ORDERS.DETAIL(id)
  );
  return response.data;
}

/**
 * Cancels an order.
 */
export async function cancelOrder(id: number): Promise<ApiResponse<Order>> {
  const response = await api.put<ApiResponse<Order>>(
    API_PATHS.ORDERS.CANCEL(id)
  );
  return response.data;
}

/**
 * Gets all orders (admin).
 */
export async function getAllOrders(
  page = 0,
  size = 12
): Promise<ApiResponse<PageResponse<Order>>> {
  const response = await api.get<ApiResponse<PageResponse<Order>>>(
    API_PATHS.ADMIN.ORDERS,
    { params: { page, size } }
  );
  return response.data;
}

/**
 * Updates an order's status (admin).
 */
export async function updateOrderStatus(
  id: number,
  data: OrderStatusUpdateRequest
): Promise<ApiResponse<Order>> {
  const response = await api.put<ApiResponse<Order>>(
    API_PATHS.ADMIN.ORDER_STATUS(id),
    data
  );
  return response.data;
}
