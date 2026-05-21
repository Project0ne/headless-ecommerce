"use client";

import { useState, useEffect } from "react";
import { useSearchParams, useRouter } from "next/navigation";
import { useProducts } from "@/hooks/useProducts";
import { getCategories } from "@/services/category-service";
import { ProductGrid } from "@/components/product/ProductGrid";
import { ProductFilters } from "@/components/product/ProductFilters";
import { PaginationComponent } from "@/components/common/Pagination";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import type { ProductFilters as ProductFiltersType } from "@/types/product";
import type { Category } from "@/types/category";

export default function ProductsPage() {
  const router = useRouter();
  const searchParams = useSearchParams();

  const keyword = searchParams.get("keyword") || undefined;
  const categoryIdParam = searchParams.get("categoryId");

  const [filters, setFilters] = useState<ProductFiltersType>({
    keyword,
    categoryId: categoryIdParam ? Number(categoryIdParam) : undefined,
    sort: "createdAt,desc",
    page: 0,
    size: 12,
  });

  const [categories, setCategories] = useState<Category[]>([]);

  useEffect(() => {
    getCategories().then((res) => {
      if (res.code === 200 && res.data) {
        setCategories(res.data);
      }
    });
  }, []);

  const { data: pageData, isLoading } = useProducts(filters);

  const handleFiltersChange = (newFilters: ProductFiltersType) => {
    setFilters({ ...newFilters, page: 0 });
    const params = new URLSearchParams();
    if (newFilters.keyword) params.set("keyword", newFilters.keyword);
    if (newFilters.categoryId) params.set("categoryId", String(newFilters.categoryId));
    if (newFilters.sort) params.set("sort", newFilters.sort);
    router.push(`/products?${params.toString()}`, { scroll: false });
  };

  const handlePageChange = (page: number) => {
    setFilters((prev) => ({ ...prev, page }));
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <div className="mb-8">
        <h1 className="text-3xl font-bold tracking-tight">Products</h1>
        {keyword && (
          <p className="mt-2 text-muted-foreground">
            Search results for &quot;{keyword}&quot;
          </p>
        )}
      </div>

      <div className="mb-6">
        <ProductFilters
          filters={filters}
          categories={categories}
          onFiltersChange={handleFiltersChange}
        />
      </div>

      {isLoading ? (
        <div className="flex justify-center py-20">
          <LoadingSpinner />
        </div>
      ) : (
        <>
          <ProductGrid
            products={pageData?.content || []}
            isLoading={false}
          />
          {pageData && (
            <PaginationComponent
              currentPage={pageData.number}
              totalPages={pageData.totalPages}
              onPageChange={handlePageChange}
            />
          )}
        </>
      )}
    </div>
  );
}
