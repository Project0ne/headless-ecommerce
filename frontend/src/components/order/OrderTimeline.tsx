import { cn } from "@/lib/utils";
import type { OrderStatus } from "@/types/order";

const steps: { status: OrderStatus; label: string }[] = [
  { status: "PENDING_PAYMENT", label: "Placed" },
  { status: "PENDING_SHIPMENT", label: "Paid" },
  { status: "SHIPPING", label: "Shipped" },
  { status: "COMPLETED", label: "Delivered" },
];

interface OrderTimelineProps {
  currentStatus: OrderStatus;
}

export function OrderTimeline({ currentStatus }: OrderTimelineProps) {
  if (currentStatus === "CANCELLED") {
    return <div className="text-sm text-destructive font-medium">Order Cancelled</div>;
  }

  const currentIndex = steps.findIndex((s) => s.status === currentStatus);

  return (
    <div className="flex items-center gap-2">
      {steps.map((step, index) => (
        <div key={step.status} className="flex items-center gap-2">
          <div className={cn("flex items-center justify-center w-8 h-8 rounded-full text-xs font-medium", index <= currentIndex ? "bg-primary text-primary-foreground" : "bg-muted text-muted-foreground")}>
            {index <= currentIndex ? "✓" : index + 1}
          </div>
          <span className={cn("text-xs", index <= currentIndex ? "text-foreground font-medium" : "text-muted-foreground")}>
            {step.label}
          </span>
          {index < steps.length - 1 && <div className={cn("h-px w-8", index < currentIndex ? "bg-primary" : "bg-muted")} />}
        </div>
      ))}
    </div>
  );
}
