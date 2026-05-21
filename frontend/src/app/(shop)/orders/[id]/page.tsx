"use client";

import { useParams, useRouter } from "next/navigation";
import { useOrder, useCancelOrder } from "@/hooks/useOrders";
import { OrderStatusBadge } from "@/components/order/OrderStatusBadge";
import { OrderItemList } from "@/components/order/OrderItemList";
import { OrderTimeline } from "@/components/order/OrderTimeline";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { ConfirmDialog } from "@/components/common/ConfirmDialog";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { formatPrice, formatDate } from "@/lib/utils";
import { useToast } from "@/hooks/use-toast";
import { useState } from "react";
import { ArrowLeft } from "lucide-react";
import Link from "next/link";

export default function OrderDetailPage() {
  const params = useParams();
  const router = useRouter();
  const orderId = Number(params.id);
  const { data: order, isLoading } = useOrder(orderId);
  const cancelOrderMutation = useCancelOrder();
  const { toast } = useToast();

  const [cancelDialogOpen, setCancelDialogOpen] = useState(false);

  const handleCancelConfirm = async () => {
    try {
      await cancelOrderMutation.mutateAsync(orderId);
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
    }
  };

  if (isLoading) {
    return (
      <div className="container mx-auto px-4 py-20 flex justify-center">
        <LoadingSpinner />
      </div>
    );
  }

  if (!order) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h1 className="text-2xl font-bold">Order not found</h1>
        <Button className="mt-4" onClick={() => router.push("/orders")}>
          Back to Orders
        </Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="flex items-center gap-4 mb-8">
        <Button variant="ghost" size="icon" onClick={() => router.back()}>
          <ArrowLeft className="h-4 w-4" />
        </Button>
        <div>
          <h1 className="text-3xl font-bold">Order #{order.orderNo}</h1>
          <p className="text-sm text-muted-foreground mt-1">
            Placed on {formatDate(order.createdAt)}
          </p>
        </div>
        <div className="ml-auto">
          <OrderStatusBadge status={order.status} />
        </div>
      </div>

      <div className="grid lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-6">
          {/* Timeline */}
          <Card>
            <CardHeader>
              <CardTitle>Order Progress</CardTitle>
            </CardHeader>
            <CardContent>
              <OrderTimeline currentStatus={order.status} />
            </CardContent>
          </Card>

          {/* Order Items */}
          <Card>
            <CardHeader>
              <CardTitle>Items</CardTitle>
            </CardHeader>
            <CardContent>
              <OrderItemList items={order.orderItems} />
              <Separator className="my-4" />
              <div className="flex justify-end">
                <div className="text-lg font-semibold">
                  Total: {formatPrice(order.totalAmount)}
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          <Card>
            <CardHeader>
              <CardTitle>Shipping Info</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3 text-sm">
              <div>
                <p className="text-muted-foreground">Name</p>
                <p className="font-medium">{order.receiverName}</p>
              </div>
              <div>
                <p className="text-muted-foreground">Phone</p>
                <p className="font-medium">{order.receiverPhone}</p>
              </div>
              <div>
                <p className="text-muted-foreground">Address</p>
                <p className="font-medium">{order.receiverAddress}</p>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Payment</CardTitle>
            </CardHeader>
            <CardContent className="space-y-3 text-sm">
              <div className="flex justify-between">
                <span className="text-muted-foreground">Status</span>
                <span className="font-medium">
                  {order.paidAt ? "Paid" : "Unpaid"}
                </span>
              </div>
              {order.paidAt && (
                <div className="flex justify-between">
                  <span className="text-muted-foreground">Paid at</span>
                  <span className="font-medium">{formatDate(order.paidAt)}</span>
                </div>
              )}
            </CardContent>
          </Card>

          {order.status === "PENDING_PAYMENT" && (
            <Button
              variant="destructive"
              className="w-full"
              onClick={() => setCancelDialogOpen(true)}
            >
              Cancel Order
            </Button>
          )}
        </div>
      </div>

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
