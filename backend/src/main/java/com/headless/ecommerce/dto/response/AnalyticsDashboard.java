package com.headless.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for analytics dashboard.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsDashboard {

    private Long totalProducts;
    private Long totalOrders;
    private Long totalUsers;
    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private Long pendingOrders;
    private Long lowStockProducts;
    private List<SalesData> salesTrend;
    private List<ProductSales> topProducts;
    private Map<String, Long> orderStatusDistribution;
    private List<CategorySales> salesByCategory;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SalesData {
        private String date;
        private BigDecimal revenue;
        private Long orderCount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductSales {
        private Long productId;
        private String productName;
        private Long salesCount;
        private BigDecimal revenue;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategorySales {
        private Long categoryId;
        private String categoryName;
        private Long productCount;
        private BigDecimal revenue;
    }
}
