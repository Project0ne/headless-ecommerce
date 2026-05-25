"use client";

import { useCart } from "@/hooks/useCart";
import { useUpdateCartItem, useRemoveCartItem } from "@/hooks/useCart";
import { useCartStore } from "@/stores/cart-store";
import { useAuthStore } from "@/stores/auth-store";
import { CartItemRow } from "@/components/cart/CartItemRow";
import { CartSummary } from "@/components/cart/CartSummary";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";

export default function CartPage() {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  const localItems = useCartStore((s) => s.items);
  const { data: cartData, isLoading } = useCart();
  const updateCartItemMutation = useUpdateCartItem();
  const removeCartItemMutation = useRemoveCartItem();

  // Use server cart if authenticated, otherwise local cart
  const items = isAuthenticated ? (cartData?.data || localItems) : localItems;

  const localUpdateQuantity = useCartStore((s) => s.updateQuantity);
  const localRemoveItem = useCartStore((s) => s.removeItem);

  const handleUpdateQuantity = (productId: number, quantity: number) => {
    if (isAuthenticated) {
      updateCartItemMutation.mutate({ productId, quantity });
    } else {
      localUpdateQuantity(productId, quantity);
    }
  };

  const handleRemove = (productId: number) => {
    if (isAuthenticated) {
      removeCartItemMutation.mutate(productId);
    } else {
      localRemoveItem(productId);
    }
  };

  if (isLoading && isAuthenticated) {
    return (
      <div className="container mx-auto px-4 py-20 flex justify-center">
        <LoadingSpinner />
      </div>
    );
  }

  if (items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8">
        <EmptyState
          title="Your cart is empty"
          description="Looks like you haven't added any items yet."
          actionLabel="Start Shopping"
          actionHref="/products"
        />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8">Shopping Cart</h1>
      <div className="grid lg:grid-cols-3 gap-8">
        {/* Cart Items */}
        <div className="lg:col-span-2">
          <div className="rounded-xl border border-border/50 bg-card p-6 shadow-card">
            {items.map((item) => (
              <CartItemRow
                key={item.productId}
                item={item}
                onUpdateQuantity={handleUpdateQuantity}
                onRemove={handleRemove}
              />
            ))}
          </div>
        </div>

        {/* Cart Summary */}
        <div>
          <CartSummary items={items} />
        </div>
      </div>
    </div>
  );
}
