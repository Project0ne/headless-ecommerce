/** Cart type definitions matching backend DTOs */

export interface CartItem {
  productId: number;
  productName: string;
  productImage: string;
  unitPrice: number;
  quantity: number;
  available: boolean;
  stock: number;
}

export interface CartItemRequest {
  productId: number;
  quantity: number;
}

export interface MergeCartRequest {
  items: CartItemRequest[];
}
