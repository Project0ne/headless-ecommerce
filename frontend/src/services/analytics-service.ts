import api from "@/lib/api";
import type { ApiResponse } from "@/types/api";

export interface SalesData {
  date: string;
  revenue: number;
  orderCount: number;
}

export interface ProductSales {
  productId: number;
  productName: string;
  salesCount: number;
  revenue: number;
}

export interface CategorySales {
  categoryId: number;
  categoryName: string;
  productCount: number;
  revenue: number;
}

export interface AnalyticsDashboard {
  totalProducts: number;
  totalOrders: number;
  totalUsers: number;
  totalRevenue: number;
  todayRevenue: number;
  pendingOrders: number;
  lowStockProducts: number;
  salesTrend: SalesData[];
  topProducts: ProductSales[];
  orderStatusDistribution: Record<string, number>;
  salesByCategory: CategorySales[];
}

/**
 * Get analytics dashboard data.
 */
export async function getAnalyticsDashboard(): Promise<ApiResponse<AnalyticsDashboard>> {
  const response = await api.get("/analytics/dashboard");
  return response.data;
}
