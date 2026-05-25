"use client";

import Link from "next/link";
import { useProducts } from "@/hooks/useProducts";
import { useCategories } from "@/hooks/useCategories";
import { ProductGrid } from "@/components/product/ProductGrid";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { ArrowRight, ShoppingBag, Truck, Shield, Headphones } from "lucide-react";

export default function HomePage() {
  const { data: productsData, isLoading: productsLoading } = useProducts({
    page: 0,
    size: 8,
    sort: "salesCount,desc",
  });
  const { data: categories, isLoading: categoriesLoading } = useCategories();

  const featuredProducts = productsData?.content ?? [];
  const categoryList = categories ?? [];
  const isLoading = productsLoading || categoriesLoading;

  return (
    <div>
      {/* Hero Section */}
      <section className="relative overflow-hidden bg-gradient-to-br from-primary/5 via-blue-50/50 to-purple-50/30 dark:from-primary/10 dark:via-slate-900/50 dark:to-purple-950/30 py-24 lg:py-32">
        <div className="absolute inset-0 bg-[radial-gradient(at_40%_20%,color-mix(in_srgb,var(--color-primary)_8%,transparent)_0px,transparent_50%),radial-gradient(at_80%_0%,hsl(280_50%_50%/0.06)_0px,transparent_50%),radial-gradient(at_0%_50%,hsl(200_60%_50%/0.06)_0px,transparent_50%)] pointer-events-none" />
        <div className="container mx-auto px-4 relative z-10">
          <div className="max-w-2xl animate-fade-in">
            <h1 className="text-5xl lg:text-6xl font-bold tracking-tight mb-6 bg-gradient-to-r from-foreground to-foreground/70 bg-clip-text text-transparent">
              Discover Amazing Products
            </h1>
            <p className="text-lg text-muted-foreground/80 mb-10 max-w-lg leading-relaxed">
              Shop the latest trends with free shipping, secure payments, and
              exceptional customer service.
            </p>
            <div className="flex gap-4">
              <Link href="/products">
                <Button size="lg">
                  <ShoppingBag className="mr-2 h-5 w-5" />
                  Shop Now
                </Button>
              </Link>
              <Link href="/products?sort=createdAt,desc">
                <Button variant="outline" size="lg">
                  New Arrivals
                </Button>
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Features Bar */}
      <section className="border-b bg-background/50">
        <div className="container mx-auto px-4 py-6">
          <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
            {[
              { icon: Truck, title: "Free Shipping", desc: "On all orders" },
              { icon: Shield, title: "Secure Payment", desc: "100% protected" },
              { icon: Headphones, title: "24/7 Support", desc: "Always here" },
              { icon: ShoppingBag, title: "Easy Returns", desc: "Hassle-free" },
            ].map(({ icon: Icon, title, desc }) => (
              <div
                key={title}
                className="flex items-center gap-3 rounded-xl bg-card/60 backdrop-blur-sm border border-border/50 shadow-elegant p-4"
              >
                <Icon className="h-8 w-8 text-primary" />
                <div>
                  <p className="text-sm font-medium">{title}</p>
                  <p className="text-xs text-muted-foreground">{desc}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Categories */}
      {categoryList.length > 0 && (
        <section className="container mx-auto px-4 py-12">
          <div className="flex items-center justify-between mb-6">
            <h2 className="text-2xl font-bold">Shop by Category</h2>
            <Link href="/products" className="text-sm text-primary hover:underline">
              View All <ArrowRight className="inline h-4 w-4" />
            </Link>
          </div>
          <div className="grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4">
            {categoryList.slice(0, 6).map((category) => (
              <Link
                key={category.id}
                href={`/products?categoryId=${category.id}`}
              >
                <Card className="group text-center hover:shadow-lifted hover:-translate-y-0.5 transition-all duration-300">
                  <CardContent className="p-6">
                    <div className="text-3xl mb-2">{category.icon || "🏷️"}</div>
                    <p className="text-sm font-medium group-hover:text-primary transition-colors">
                      {category.name}
                    </p>
                  </CardContent>
                </Card>
              </Link>
            ))}
          </div>
        </section>
      )}

      {/* Featured Products */}
      <section className="container mx-auto px-4 py-12">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold">Best Sellers</h2>
          <Link href="/products?sort=salesCount,desc" className="text-sm text-primary hover:underline">
            View All <ArrowRight className="inline h-4 w-4" />
          </Link>
        </div>
        {isLoading ? (
          <div className="flex justify-center py-12">
            <LoadingSpinner />
          </div>
        ) : (
          <ProductGrid products={featuredProducts} />
        )}
      </section>

      {/* CTA Section */}
      <section className="bg-gradient-to-r from-primary/5 via-blue-50/40 to-primary/5 dark:from-primary/10 dark:via-slate-900/40 dark:to-primary/10 py-16">
        <div className="container mx-auto px-4 text-center">
          <h2 className="text-3xl font-bold mb-4">Ready to Start Shopping?</h2>
          <p className="text-muted-foreground mb-8 max-w-md mx-auto">
            Join thousands of happy customers and discover products you&apos;ll love.
          </p>
          <Link href="/products">
            <Button size="lg">
              Browse All Products
              <ArrowRight className="ml-2 h-5 w-5" />
            </Button>
          </Link>
        </div>
      </section>
    </div>
  );
}
