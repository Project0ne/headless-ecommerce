"use client";

import Link from "next/link";
import { ShoppingCart } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useCartStore } from "@/stores/cart-store";
import { useAuthStore } from "@/stores/auth-store";
import { useCart } from "@/hooks/useCart";

export function CartIcon() {
  const localItemCount = useCartStore((s) => s.getTotalItems());
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  const { data: cartData } = useCart();

  // Use API cart count for authenticated users, local store for guests
  const apiItemCount = cartData?.data
    ? cartData.data.reduce((sum, item) => sum + item.quantity, 0)
    : 0;
  const count = isAuthenticated ? apiItemCount : localItemCount;

  return (
    <Link href="/cart">
      <Button variant="ghost" size="icon" className="relative">
        <ShoppingCart className="h-5 w-5" />
        {count > 0 && (
          <span className="absolute -top-1 -right-1 h-5 w-5 rounded-full bg-primary text-[10px] font-bold text-primary-foreground flex items-center justify-center">
            {count > 99 ? "99+" : count}
          </span>
        )}
      </Button>
    </Link>
  );
}
