import { formatPrice } from "@/lib/utils";
import type { OrderItem } from "@/types/order";

interface OrderItemListProps {
  items: OrderItem[];
}

export function OrderItemList({ items }: OrderItemListProps) {
  return (
    <div className="space-y-3">
      {items.map((item) => (
        <div key={item.id} className="flex items-center gap-4">
          <div className="h-16 w-16 flex-shrink-0 overflow-hidden rounded-md bg-muted">
            {item.productImage ? (
              <img src={item.productImage} alt={item.productName} className="h-full w-full object-cover" />
            ) : (
              <div className="flex h-full items-center justify-center text-xs text-muted-foreground">N/A</div>
            )}
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium truncate">{item.productName}</p>
            <p className="text-xs text-muted-foreground">{formatPrice(item.unitPrice)} x {item.quantity}</p>
          </div>
          <div className="text-sm font-medium">{formatPrice(item.subtotal)}</div>
        </div>
      ))}
    </div>
  );
}
