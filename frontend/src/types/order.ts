/** Order type definitions matching backend DTOs */

export type OrderStatus =
  | "PENDING_PAYMENT"
  | "PENDING_SHIPMENT"
  | "SHIPPING"
  | "COMPLETED"
  | "CANCELLED";

export interface Order {
  id: number;
  orderNo: string;
  totalAmount: number;
  status: OrderStatus;
  receiverName: string;
  receiverPhone: string;
  receiverAddress: string;
  paidAt: string | null;
  createdAt: string;
  updatedAt: string;
  orderItems: OrderItem[];
}

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  productImage: string;
  unitPrice: number;
  quantity: number;
  subtotal: number;
}

export interface OrderCreateRequest {
  receiverName: string;
  receiverPhone: string;
  receiverAddress: string;
}

export interface OrderStatusUpdateRequest {
  status: OrderStatus;
}
