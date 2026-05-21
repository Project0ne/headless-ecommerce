"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Trash2, Minus, Plus } from "lucide-react";
import { formatPrice } from "@/lib/utils";
import type { CartItem } from "@/types/cart";

interface CartItemRowProps {
  item: CartItem;
  onUpdateQuantity: (productId: number, quantity: number) => void;
  onRemove: (productId: number) => void;
}

export function CartItemRow({ item, onUpdateQuantity, onRemove }: CartItemRowProps) {
  return (
    <div className="flex items-center gap-4 py-4 border-b last:border-0">
      <div className="h-20 w-20 flex-shrink-0 overflow-hidden rounded-md bg-muted">
        {item.productImage ? (
          <img src={item.productImage} alt={item.productName} className="h-full w-full object-cover" />
        ) : (
          <div className="flex h-full items-center justify-center text-xs text-muted-foreground">No Image</div>
        )}
      </div>
      <div className="flex-1 min-w-0">
        <h3 className="font-medium text-sm truncate">{item.productName}</h3>
        <p className="text-sm font-bold text-primary mt-1">{formatPrice(item.unitPrice)}</p>
        {!item.available && <p className="text-xs text-destructive mt-1">Out of stock</p>}
      </div>
      <div className="flex items-center gap-2">
        <Button variant="outline" size="icon" className="h-8 w-8" onClick={() => onUpdateQuantity(item.productId, Math.max(1, item.quantity - 1))} disabled={item.quantity <= 1}>
          <Minus className="h-3 w-3" />
        </Button>
        <span className="w-8 text-center text-sm">{item.quantity}</span>
        <Button variant="outline" size="icon" className="h-8 w-8" onClick={() => onUpdateQuantity(item.productId, item.quantity + 1)}>
          <Plus className="h-3 w-3" />
        </Button>
      </div>
      <div className="w-24 text-right">
        <p className="font-medium text-sm">{formatPrice(item.unitPrice * item.quantity)}</p>
      </div>
      <Button variant="ghost" size="icon" className="h-8 w-8 text-muted-foreground hover:text-destructive" onClick={() => onRemove(item.productId)}>
        <Trash2 className="h-4 w-4" />
      </Button>
    </div>
  );
}
