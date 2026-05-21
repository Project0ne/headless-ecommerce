"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useCart } from "@/hooks/useCart";
import { useCreateOrder } from "@/hooks/useOrders";
import { useCartStore } from "@/stores/cart-store";
import { useAuthStore } from "@/stores/auth-store";
import { processPayment } from "@/services/payment-service";
import { CartItemRow } from "@/components/cart/CartItemRow";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { formatPrice } from "@/lib/utils";
import { useToast } from "@/hooks/use-toast";
import Link from "next/link";

const checkoutSchema = z.object({
  receiverName: z.string().min(2, "Name must be at least 2 characters"),
  receiverPhone: z.string().min(8, "Phone must be at least 8 characters"),
  receiverAddress: z.string().min(5, "Address must be at least 5 characters"),
});

type CheckoutFormValues = z.infer<typeof checkoutSchema>;

export default function CheckoutPage() {
  const router = useRouter();
  const { toast } = useToast();
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  const localItems = useCartStore((s) => s.items);
  const { data: cartData } = useCart();
  const createOrderMutation = useCreateOrder();

  const [isProcessing, setIsProcessing] = useState(false);

  const items = isAuthenticated ? (cartData?.data || localItems) : localItems;
  const totalPrice = items.reduce(
    (sum, item) => sum + item.unitPrice * item.quantity,
    0
  );

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CheckoutFormValues>({
    resolver: zodResolver(checkoutSchema),
    defaultValues: {
      receiverName: "",
      receiverPhone: "",
      receiverAddress: "",
    },
  });

  const onSubmit = async (data: CheckoutFormValues) => {
    try {
      setIsProcessing(true);
      // Create order
      const orderResponse = await createOrderMutation.mutateAsync(data);
      if (orderResponse.code === 200 && orderResponse.data) {
        // Process payment
        const paymentResponse = await processPayment(orderResponse.data.orderNo);
        if (paymentResponse.code === 200) {
          toast({
            title: "Order placed successfully!",
            description: `Order #${orderResponse.data.orderNo} has been created and paid.`,
          });
          router.push(`/orders/${orderResponse.data.id}`);
        } else {
          toast({
            title: "Order created, payment pending",
            description: `Order #${orderResponse.data.orderNo} created. Please complete payment.`,
          });
          router.push(`/orders/${orderResponse.data.id}`);
        }
      }
    } catch {
      toast({
        title: "Checkout failed",
        description: "Something went wrong. Please try again.",
        variant: "destructive",
      });
    } finally {
      setIsProcessing(false);
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h1 className="text-2xl font-bold mb-4">Please sign in to checkout</h1>
        <Link href="/auth/login">
          <Button>Sign In</Button>
        </Link>
      </div>
    );
  }

  if (items.length === 0) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h1 className="text-2xl font-bold mb-4">Your cart is empty</h1>
        <Link href="/products">
          <Button>Continue Shopping</Button>
        </Link>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-3xl font-bold mb-8 tracking-tight">Checkout</h1>

      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="grid lg:grid-cols-3 gap-8">
          {/* Shipping Info */}
          <div className="lg:col-span-2 space-y-6">
            <Card className="border-border/50">
              <CardHeader>
                <CardTitle>Shipping Information</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div>
                  <label className="text-sm font-medium">Full Name</label>
                  <Input {...register("receiverName")} placeholder="Enter your name" className="mt-1.5" />
                  {errors.receiverName && (
                    <p className="text-sm text-destructive mt-1">
                      {errors.receiverName.message}
                    </p>
                  )}
                </div>
                <div>
                  <label className="text-sm font-medium">Phone Number</label>
                  <Input {...register("receiverPhone")} placeholder="Enter your phone" className="mt-1.5" />
                  {errors.receiverPhone && (
                    <p className="text-sm text-destructive mt-1">
                      {errors.receiverPhone.message}
                    </p>
                  )}
                </div>
                <div>
                  <label className="text-sm font-medium">Shipping Address</label>
                  <Input {...register("receiverAddress")} placeholder="Enter your address" className="mt-1.5" />
                  {errors.receiverAddress && (
                    <p className="text-sm text-destructive mt-1">
                      {errors.receiverAddress.message}
                    </p>
                  )}
                </div>
              </CardContent>
            </Card>

            {/* Order Items */}
            <Card className="border-border/50">
              <CardHeader>
                <CardTitle>Order Items</CardTitle>
              </CardHeader>
              <CardContent>
                {items.map((item) => (
                  <div key={item.productId} className="flex items-center gap-4 py-3 border-b border-border/50 last:border-0">
                    <div className="h-16 w-16 flex-shrink-0 overflow-hidden rounded-lg bg-muted">
                      {item.productImage ? (
                        <img src={item.productImage} alt={item.productName} className="h-full w-full object-cover" />
                      ) : (
                        <div className="flex h-full items-center justify-center text-xs text-muted-foreground">N/A</div>
                      )}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium truncate">{item.productName}</p>
                      <p className="text-xs text-muted-foreground">
                        {formatPrice(item.unitPrice)} x {item.quantity}
                      </p>
                    </div>
                    <div className="text-sm font-semibold">
                      {formatPrice(item.unitPrice * item.quantity)}
                    </div>
                  </div>
                ))}
              </CardContent>
            </Card>
          </div>

          {/* Order Summary */}
          <div>
            <Card className="border-border/50">
              <CardHeader>
                <CardTitle>Order Summary</CardTitle>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="flex justify-between text-sm">
                  <span className="text-muted-foreground">
                    Items ({items.reduce((s, i) => s + i.quantity, 0)})
                  </span>
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
                <Button
                  type="submit"
                  className="w-full shadow-button"
                  size="lg"
                  disabled={isProcessing}
                >
                  {isProcessing ? (
                    <div className="flex items-center gap-2">
                      <LoadingSpinner />
                      Processing...
                    </div>
                  ) : (
                    "Place Order"
                  )}
                </Button>
              </CardContent>
            </Card>
          </div>
        </div>
      </form>
    </div>
  );
}
