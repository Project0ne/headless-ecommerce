"use client";

import { useParams, useRouter } from "next/navigation";
import { useOrder, useUpdateOrderStatus } from "@/hooks/useOrders";
import { OrderStatusBadge } from "@/components/order/OrderStatusBadge";
import { OrderItemList } from "@/components/order/OrderItemList";
import { OrderTimeline } from "@/components/order/OrderTimeline";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { formatPrice, formatDate } from "@/lib/utils";
import { useToast } from "@/hooks/use-toast";
import { ArrowLeft } from "lucide-react";
import type { OrderStatus } from "@/types/order";

const statusLabels: Record<OrderStatus, string> = {
  PENDING_PAYMENT: "Pending Payment",
  PENDING_SHIPMENT: "Pending Shipment",
  SHIPPING: "Shipping",
  COMPLETED: "Completed",
  CANCELLED: "Cancelled",
};

export default function AdminOrderDetailPage() {
  const params = useParams();
  const router = useRouter();
  const orderId = Number(params.id);
  const { data: order, isLoading } = useOrder(orderId);
  const updateStatusMutation = useUpdateOrderStatus();
  const { toast } = useToast();

  const handleStatusChange = async (newStatus: OrderStatus) => {
    try {
      await updateStatusMutation.mutateAsync({
        id: orderId,
        data: { status: newStatus },
      });
      toast({
        title: "Order status updated",
        description: `Order has been updated to ${statusLabels[newStatus]}.`,
      });
    } catch {
      toast({
        title: "Error",
        description: "Failed to update order status.",
        variant: "destructive",
      });
    }
  };

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner />
      </div>
    );
  }

  if (!order) {
    return (
      <div className="text-center py-20">
        <h1 className="text-2xl font-bold">Order not found</h1>
        <Button className="mt-4" onClick={() => router.push("/admin/orders")}>
          Back to Orders
        </Button>
      </div>
    );
  }

  return (
    <div>
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

          {/* Admin Actions */}
          <Card>
            <CardHeader>
              <CardTitle>Actions</CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              {order.status === "PENDING_PAYMENT" && (
                <Button
                  className="w-full"
                  onClick={() => handleStatusChange("PENDING_SHIPMENT")}
                  disabled={updateStatusMutation.isPending}
                >
                  Mark as Paid
                </Button>
              )}
              {order.status === "PENDING_SHIPMENT" && (
                <Button
                  className="w-full"
                  onClick={() => handleStatusChange("SHIPPING")}
                  disabled={updateStatusMutation.isPending}
                >
                  Ship Order
                </Button>
              )}
              {order.status === "SHIPPING" && (
                <Button
                  className="w-full"
                  onClick={() => handleStatusChange("COMPLETED")}
                  disabled={updateStatusMutation.isPending}
                >
                  Mark as Completed
                </Button>
              )}
              {(order.status === "PENDING_PAYMENT" ||
                order.status === "PENDING_SHIPMENT") && (
                <Button
                  variant="destructive"
                  className="w-full"
                  onClick={() => handleStatusChange("CANCELLED")}
                  disabled={updateStatusMutation.isPending}
                >
                  Cancel Order
                </Button>
              )}
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
}
