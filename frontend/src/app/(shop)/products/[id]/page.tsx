"use client";

import { useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { useProduct } from "@/hooks/useProducts";
import { useAddToCart } from "@/hooks/useCart";
import { useAuthStore } from "@/stores/auth-store";
import { ProductImageGallery } from "@/components/product/ProductImageGallery";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import { Minus, Plus, ShoppingCart } from "lucide-react";
import { formatPrice } from "@/lib/utils";
import { useToast } from "@/hooks/use-toast";

export default function ProductDetailPage() {
  const params = useParams();
  const router = useRouter();
  const productId = Number(params.id);
  const { data: product, isLoading } = useProduct(productId);
  const addToCartMutation = useAddToCart();
  const isAuthenticated = useAuthStore((s) => s.isAuthenticated);
  const { toast } = useToast();

  const [quantity, setQuantity] = useState(1);

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      router.push("/auth/login");
      return;
    }
    try {
      await addToCartMutation.mutateAsync({
        productId,
        quantity,
      });
      toast({
        title: "Added to cart",
        description: `${quantity} item(s) added to your cart.`,
      });
    } catch {
      toast({
        title: "Error",
        description: "Failed to add item to cart.",
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

  if (!product) {
    return (
      <div className="container mx-auto px-4 py-20 text-center">
        <h1 className="text-2xl font-bold">Product not found</h1>
        <Button className="mt-4" onClick={() => router.push("/products")}>
          Back to Products
        </Button>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="grid md:grid-cols-2 gap-8 lg:gap-12">
        {/* Image */}
        <ProductImageGallery imageUrl={product.imageUrl} name={product.name} />

        {/* Details */}
        <div className="space-y-6">
          <div>
            <h1 className="text-3xl md:text-4xl font-bold tracking-tight">{product.name}</h1>
            <div className="mt-3 flex items-center gap-3">
              <Badge variant="secondary" className="rounded-lg">{product.categoryName}</Badge>
              {product.salesCount > 0 && (
                <span className="text-sm text-muted-foreground">
                  {product.salesCount} sold
                </span>
              )}
            </div>
          </div>

          <div className="text-4xl font-bold bg-gradient-to-r from-primary to-primary/70 bg-clip-text text-transparent">
            {formatPrice(product.price)}
          </div>

          <Separator className="bg-border/50" />

          <div>
            <h3 className="font-semibold mb-2">Description</h3>
            <p className="text-sm text-muted-foreground whitespace-pre-line leading-relaxed">
              {product.description || "No description available."}
            </p>
          </div>

          <Separator className="bg-border/50" />

          <div>
            <h3 className="font-semibold mb-2">Availability</h3>
            {product.stock > 0 ? (
              <div className="flex items-center gap-2">
                <span className="h-2 w-2 rounded-full bg-green-500" />
                <p className="text-sm text-green-600 font-medium">In stock ({product.stock} available)</p>
              </div>
            ) : (
              <div className="flex items-center gap-2">
                <span className="h-2 w-2 rounded-full bg-destructive" />
                <p className="text-sm text-destructive font-medium">Out of stock</p>
              </div>
            )}
          </div>

          <Separator className="bg-border/50" />

          {/* Quantity & Add to Cart */}
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-1 rounded-xl border bg-secondary/50 p-1">
              <Button
                variant="ghost"
                size="icon"
                className="h-9 w-9 rounded-lg"
                onClick={() => setQuantity(Math.max(1, quantity - 1))}
                disabled={quantity <= 1}
              >
                <Minus className="h-4 w-4" />
              </Button>
              <span className="w-10 text-center text-lg font-semibold tabular-nums">{quantity}</span>
              <Button
                variant="ghost"
                size="icon"
                className="h-9 w-9 rounded-lg"
                onClick={() => setQuantity(Math.min(product.stock, quantity + 1))}
                disabled={quantity >= product.stock}
              >
                <Plus className="h-4 w-4" />
              </Button>
            </div>

            <Button
              size="lg"
              className="flex-1 shadow-button"
              onClick={handleAddToCart}
              disabled={product.stock === 0 || addToCartMutation.isPending}
            >
              <ShoppingCart className="mr-2 h-5 w-5" />
              {addToCartMutation.isPending ? "Adding..." : "Add to Cart"}
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
