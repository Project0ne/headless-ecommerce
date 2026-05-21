import { create } from "zustand";
import { persist } from "zustand/middleware";
import type { CartItem, CartItemRequest } from "@/types/cart";

/** Cart state interface */
interface CartState {
  items: CartItem[];
  setItems: (items: CartItem[]) => void;
  addItem: (item: CartItemRequest) => void;
  updateQuantity: (productId: number, quantity: number) => void;
  removeItem: (productId: number) => void;
  clearCart: () => void;
  getTotalItems: () => number;
  getTotalPrice: () => number;
}

/**
 * Zustand store for local cart state (used when not authenticated).
 * When authenticated, the cart is managed by the backend via Redis.
 */
export const useCartStore = create<CartState>()(
  persist(
    (set, get) => ({
      items: [],

      setItems: (items: CartItem[]) => {
        set({ items });
      },

      addItem: (item: CartItemRequest) => {
        set((state) => {
          const existing = state.items.find(
            (i) => i.productId === item.productId
          );
          if (existing) {
            return {
              items: state.items.map((i) =>
                i.productId === item.productId
                  ? { ...i, quantity: i.quantity + item.quantity }
                  : i
              ),
            };
          }
          // When adding locally, we don't have full product details yet
          const newItem: CartItem = {
            productId: item.productId,
            productName: "",
            productImage: "",
            unitPrice: 0,
            quantity: item.quantity,
            available: true,
            stock: 0,
          };
          return { items: [...state.items, newItem] };
        });
      },

      updateQuantity: (productId: number, quantity: number) => {
        set((state) => ({
          items: state.items.map((i) =>
            i.productId === productId ? { ...i, quantity } : i
          ),
        }));
      },

      removeItem: (productId: number) => {
        set((state) => ({
          items: state.items.filter((i) => i.productId !== productId),
        }));
      },

      clearCart: () => {
        set({ items: [] });
      },

      getTotalItems: () => {
        return get().items.reduce((total, item) => total + item.quantity, 0);
      },

      getTotalPrice: () => {
        return get().items.reduce(
          (total, item) => total + item.unitPrice * item.quantity,
          0
        );
      },
    }),
    {
      name: "cart-storage",
      partialize: (state) => ({
        items: state.items.map((item) => ({
          productId: item.productId,
          quantity: item.quantity,
          productName: "",
          productImage: "",
          unitPrice: 0,
          available: true,
          stock: 0,
        })),
      }),
    }
  )
);
