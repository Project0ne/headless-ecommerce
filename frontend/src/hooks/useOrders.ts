import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  getUserOrders,
  getOrderById,
  createOrder,
  cancelOrder,
  getAllOrders,
  updateOrderStatus,
} from "@/services/order-service";
import type { OrderCreateRequest, OrderStatusUpdateRequest } from "@/types/order";

/**
 * Hook for fetching the user's orders.
 */
export function useUserOrders(page = 0, size = 12) {
  return useQuery({
    queryKey: ["userOrders", page, size],
    queryFn: () => getUserOrders(page, size),
    select: (response) => response.data,
  });
}

/**
 * Hook for fetching a single order by ID.
 */
export function useOrder(id: number) {
  return useQuery({
    queryKey: ["order", id],
    queryFn: () => getOrderById(id),
    select: (response) => response.data,
    enabled: !!id,
  });
}

/**
 * Hook for creating a new order.
 */
export function useCreateOrder() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["userOrders"] });
      queryClient.invalidateQueries({ queryKey: ["cart"] });
    },
  });
}

/**
 * Hook for cancelling an order.
 */
export function useCancelOrder() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: cancelOrder,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["userOrders"] });
    },
  });
}

/**
 * Hook for fetching all orders (admin).
 */
export function useAllOrders(page = 0, size = 12) {
  return useQuery({
    queryKey: ["allOrders", page, size],
    queryFn: () => getAllOrders(page, size),
    select: (response) => response.data,
  });
}

/**
 * Hook for updating an order's status (admin).
 */
export function useUpdateOrderStatus() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      id,
      data,
    }: {
      id: number;
      data: OrderStatusUpdateRequest;
    }) => updateOrderStatus(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["allOrders"] });
    },
  });
}
