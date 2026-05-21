"use client";

import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { formatPrice } from "@/lib/utils";
import type { CartItem } from "@/types/cart";
import Link from "next/link";

interface CartSummaryProps {
  items: CartItem[];
}

export function CartSummary({ items }: CartSummaryProps) {
  const totalItems = items.reduce((sum, item) => sum + item.quantity, 0);
  const totalPrice = items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0);

  return (
    <div className="rounded-xl border border-border/50 p-6 space-y-4 shadow-card bg-card">
      <h2 className="text-lg font-semibold">Order Summary</h2>
      <Separator className="bg-border/50" />
      <div className="flex justify-between text-sm">
        <span className="text-muted-foreground">Items ({totalItems})</span>
        <span className="font-medium">{formatPrice(totalPrice)}</span>
      </div>
      <div className="flex justify-between text-sm">
        <span className="text-muted-foreground">Shipping</span>
        <span className="font-medium text-green-600">Free</span>
      </div>
      <Separator className="bg-border/50" />
      <div className="flex justify-between font-semibold text-lg">
        <span>Total</span>
        <span className="bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">{formatPrice(totalPrice)}</span>
      </div>
      <Link href="/checkout" className="block">
        <Button className="w-full shadow-button" size="lg" disabled={items.length === 0}>
          Proceed to Checkout
        </Button>
      </Link>
    </div>
  );
}
