"use client";

import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Button } from "@/components/ui/button";
import { X } from "lucide-react";
import type { ProductFilters } from "@/types/product";
import type { Category } from "@/types/category";

interface ProductFiltersProps {
  filters: ProductFilters;
  categories: Category[];
  onFiltersChange: (filters: ProductFilters) => void;
}

export function ProductFilters({ filters, categories, onFiltersChange }: ProductFiltersProps) {
  const hasFilters = filters.keyword || filters.categoryId;

  return (
    <div className="flex flex-wrap items-center gap-3">
      <Select
        value={filters.categoryId?.toString() || "all"}
        onValueChange={(value) =>
          onFiltersChange({ ...filters, categoryId: value === "all" ? undefined : Number(value) })
        }
      >
        <SelectTrigger className="w-[180px]">
          <SelectValue placeholder="All Categories" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value="all">All Categories</SelectItem>
          {categories.map((cat) => (
            <SelectItem key={cat.id} value={cat.id.toString()}>
              {cat.name}
            </SelectItem>
          ))}
        </SelectContent>
      </Select>

      <Select
        value={filters.sort || "createdAt,desc"}
        onValueChange={(value) => onFiltersChange({ ...filters, sort: value })}
      >
        <SelectTrigger className="w-[180px]">
          <SelectValue placeholder="Sort by" />
        </SelectTrigger>
        <SelectContent>
          <SelectItem value="createdAt,desc">Newest First</SelectItem>
          <SelectItem value="createdAt,asc">Oldest First</SelectItem>
          <SelectItem value="price,asc">Price: Low to High</SelectItem>
          <SelectItem value="price,desc">Price: High to Low</SelectItem>
          <SelectItem value="salesCount,desc">Best Selling</SelectItem>
        </SelectContent>
      </Select>

      {hasFilters && (
        <Button variant="ghost" size="sm" onClick={() => onFiltersChange({})}>
          <X className="h-4 w-4 mr-1" /> Clear Filters
        </Button>
      )}
    </div>
  );
}
