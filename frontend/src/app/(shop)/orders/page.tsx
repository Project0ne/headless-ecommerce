"use client";

import { useState } from "react";
import { useUserOrders, useCancelOrder } from "@/hooks/useOrders";
import { useAuthStore } from "@/stores/auth-store";
import { OrderStatusBadge } from "@/components/order/OrderStatusBadge";
import { OrderItemList } from "@/components/order/OrderItemList";
import { PaginationComponent } from "@/components/common/Pagination";
import { EmptyState } from "@/components/common/EmptyState";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { formatPrice, formatDate } from "@/lib/utils";
import { useToast } from "@/hooks/use-toast";
import Link from "next/link";

export default function OrdersPage() {
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  const [page, setPage] = useState(0);
  const { data: ordersData, isLoading } = useUserOrders(page, 10);
  const cancelOrderMutation = useCancelOrder();
  const { toast } = useToast();

  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);
  const [cancellingOrderId, setCancellingOrderId] = useState<number | null>(null);

  const handleCancelClick = (orderId: number) => {
    setCancellingOrderId(orderId);
    setCancelDialogOpen(true);
  };

  const handleCancelConfirm = async () => {
    if (cancellingOrderId === null) return;
    try {
      await cancelOrderMutation.mutateAsync(cancellingOrderId);
      toast({
        title: "Order cancelled",
        description: "The order has been cancelled successfully.",
      });
    } catch {
      toast({
        title: "Error",
        description: "Failed to cancel the order.",
        variant: "destructive",
      });
    } finally {
      setCancelDialogOpen(false);
      setCancellingOrderId(null);
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h1 className="text-2xl font-bold mb-4">Please sign in to view orders</h1>
        <Link href="/auth/login">
          <Button>Sign In</Button>
        </Link>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-20 flex justify-center">
        <LoadingSpinner />
      </div>
    );
  }

  const orders = ordersData?.content || [];

  if (orders.length === 0) {
    return (
      <div className="container mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold mb-8 tracking-tight">My Orders</h1>
        <EmptyState
          title="No orders yet"
          description="You haven't placed any orders."
          actionLabel="Start Shopping"
          actionHref="/products"
        />
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8 tracking-tight">My Orders</h1>

      <div className="space-y-4">
        {orders.map((order) => (
          <Card key={order.id} className="border-border/50">
            <CardContent className="p-6">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-4">
                  <Link
                    href={`/orders/${order.id}`}
                    className="text-sm font-semibold hover:text-primary transition-colors"
                  >
                    #{order.orderNo}
                  </Link>
                  <OrderStatusBadge status={order.status} />
                </div>
                <div className="text-sm text-muted-foreground">
                  {formatDate(order.createdAt)}
                </div>
              </div>

              <OrderItemList items={order.orderItems} />

              <Separator className="my-4 bg-border/50" />

              <div className="flex items-center justify-between">
                <div className="font-semibold">
                  Total: {formatPrice(order.totalAmount)}
                </div>
                <div className="flex items-center gap-2">
                  {order.status === "PENDING_PAYMENT" && (
                    <Button
                      variant="destructive"
                      size="sm"
                      onClick={() => handleCancelClick(order.id)}
                    >
                      Cancel Order
                    </Button>
                  )}
                  <Link href={`/orders/${order.id}`}>
                    <Button variant="outline" size="sm">
                      View Details
                    </Button>
                  </Link>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {ordersData && (
        <PaginationComponent
          currentPage={ordersData.number}
          totalPages={ordersData.totalPages}
          onPageChange={setPage}
        />
      )}

      <ConfirmDialog
        open={cancelDialogOpen}
        onOpenChange={setCancelDialogOpen}
        title="Cancel Order"
        description="Are you sure you want to cancel this order? This action cannot be undone."
        onConfirm={handleCancelConfirm}
        confirmLabel="Cancel Order"
        cancelLabel="Keep Order"
      />
    </div>
  );
}
