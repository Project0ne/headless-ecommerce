"use client";

import Link from "next/link";
import { Card, CardContent } from "@/components/ui/card";
import { formatPrice } from "@/lib/utils";
import type { Product } from "@/types/product";

interface ProductCardProps {
  product: Product;
}

export function ProductCard({ product }: ProductCardProps) {
  return (
    <Link href={`/products/${product.id}`}>
      <Card className="group overflow-hidden rounded-xl border-0 shadow-card hover:shadow-card-hover transition-all duration-300 hover:-translate-y-1 bg-card hover-lift">
        <div className="relative aspect-square overflow-hidden bg-muted">
          {product.imageUrl ? (
            <img
              src={product.imageUrl}
              alt={product.name}
              className="h-full w-full object-cover transition-transform duration-500 group-hover:scale-105"
            />
          ) : (
            <div className="flex h-full items-center justify-center text-muted-foreground">
              No Image
            </div>
          )}
          <div className="absolute inset-0 bg-gradient-to-t from-black/20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 pointer-events-none" />
        </div>
        <CardContent className="p-4">
          <h3 className="font-medium text-sm line-clamp-2 min-h-[2.5rem]">{product.name}</h3>
          <span className="mt-2 inline-block text-lg font-bold text-gradient-primary bg-primary/5 rounded-md px-2 py-0.5">
            {formatPrice(product.price)}
          </span>
          {product.salesCount > 0 && (
            <p className="mt-1 text-xs text-muted-foreground/60">{product.salesCount} sold</p>
          )}
        </CardContent>
      </Card>
    </Link>
  );
}
