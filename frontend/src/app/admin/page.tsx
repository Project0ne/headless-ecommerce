"use client";

import { useQuery } from "@tanstack/react-query";
import { getAnalyticsDashboard } from "@/services/analytics-service";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { LoadingSpinner } from "@/components/common/LoadingSpinner";
import { SalesChart, TopProductsChart, OrderStatusChart } from "@/components/admin/SalesChart";
import { Package, ShoppingCart, DollarSign, Users, AlertTriangle, Clock } from "lucide-react";
import { formatPrice } from "@/lib/utils";
import Link from "next/link";

export default function AdminDashboardPage() {
  const { data: analytics, isLoading } = useQuery({
    queryKey: ["admin-analytics"],
    queryFn: async () => {
      const res = await getAnalyticsDashboard();
      if (res.code === 200 && res.data) {
        return res.data;
      }
      throw new Error("Failed to fetch analytics");
    },
  });

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner />
      </div>
    );
  }

  const statCards = [
    { title: "Total Revenue", value: formatPrice(analytics?.totalRevenue ?? 0), icon: DollarSign, trend: `Today: ${formatPrice(analytics?.todayRevenue ?? 0)}` },
    { title: "Total Orders", value: String(analytics?.totalOrders ?? 0), icon: ShoppingCart, trend: `${analytics?.pendingOrders ?? 0} pending` },
    { title: "Total Products", value: String(analytics?.totalProducts ?? 0), icon: Package, trend: `${analytics?.lowStockProducts ?? 0} low stock` },
    { title: "Total Users", value: String(analytics?.totalUsers ?? 0), icon: Users, trend: "Active" },
  ];

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold mb-8">Dashboard</h1>

        {/* Stats Cards */}
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {statCards.map((card) => (
            <Card key={card.title}>
              <CardHeader className="flex flex-row items-center justify-between pb-2">
                <CardTitle className="text-sm font-medium text-muted-foreground">
                  {card.title}
                </CardTitle>
                <card.icon className="h-4 w-4 text-muted-foreground" />
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{card.value}</div>
                <p className="text-xs text-muted-foreground mt-1">{card.trend}</p>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* Alerts */}
        {(analytics?.pendingOrders ?? 0) > 0 && (
          <div className="mt-4 flex items-center gap-2 rounded-lg border border-yellow-200 bg-yellow-50 p-3 dark:border-yellow-900 dark:bg-yellow-950">
            <Clock className="h-4 w-4 text-yellow-600" />
            <span className="text-sm text-yellow-800 dark:text-yellow-200">
              {analytics?.pendingOrders} orders awaiting payment
            </span>
            <Link href="/admin/orders" className="ml-auto">
              <Button size="sm" variant="outline">View Orders</Button>
            </Link>
          </div>
        )}

        {(analytics?.lowStockProducts ?? 0) > 0 && (
          <div className="mt-2 flex items-center gap-2 rounded-lg border border-orange-200 bg-orange-50 p-3 dark:border-orange-900 dark:bg-orange-950">
            <AlertTriangle className="h-4 w-4 text-orange-600" />
            <span className="text-sm text-orange-800 dark:text-orange-200">
              {analytics?.lowStockProducts} products with low stock
            </span>
            <Link href="/admin/products" className="ml-auto">
              <Button size="sm" variant="outline">Check Stock</Button>
            </Link>
          </div>
        )}
      </div>

      {/* Charts */}
      <div className="grid lg:grid-cols-2 gap-6">
        {analytics?.salesTrend && <SalesChart data={analytics.salesTrend} />}
        {analytics?.orderStatusDistribution && <OrderStatusChart data={analytics.orderStatusDistribution} />}
      </div>

      {analytics?.topProducts && analytics.topProducts.length > 0 && (
        <TopProductsChart data={analytics.topProducts} />
      )}

      {/* Quick Actions */}
      <Card>
        <CardHeader>
          <CardTitle>Quick Actions</CardTitle>
        </CardHeader>
        <CardContent className="flex flex-wrap gap-3">
          <Button asChild>
            <Link href="/admin/products/new">Add New Product</Link>
          </Button>
          <Button variant="outline" asChild>
            <Link href="/admin/categories">Manage Categories</Link>
          </Button>
          <Button variant="outline" asChild>
            <Link href="/admin/orders">View Orders</Link>
          </Button>
        </CardContent>
      </Card>
    </div>
  );
}
