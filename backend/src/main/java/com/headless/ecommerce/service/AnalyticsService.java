package com.headless.ecommerce.service;

import com.headless.ecommerce.dto.response.AnalyticsDashboard;

/**
 * Service interface for analytics and reporting.
 */
public interface AnalyticsService {

    /**
     * Get complete dashboard analytics data.
     */
    AnalyticsDashboard getDashboardStats();

    /**
     * Get sales data for chart (last N days).
     */
    java.util.List<AnalyticsDashboard.SalesData> getSalesTrend(int days);

    /**
     * Get top selling products.
     */
    java.util.List<AnalyticsDashboard.ProductSales> getTopProducts(int limit);

    /**
     * Get order distribution by status.
     */
    java.util.Map<String, Long> getOrderStatusDistribution();

    /**
     * Get sales by category.
     */
    java.util.List<AnalyticsDashboard.CategorySales> getSalesByCategory();
}
