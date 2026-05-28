package com.headless.ecommerce.service.impl;

import com.headless.ecommerce.dto.response.AnalyticsDashboard;
import com.headless.ecommerce.model.Order;
import com.headless.ecommerce.model.enums.OrderStatus;
import com.headless.ecommerce.repository.*;
import com.headless.ecommerce.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of AnalyticsService.
 */
@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public AnalyticsDashboard getDashboardStats() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();

        BigDecimal totalRevenue = orderRepository.findAll().stream()
            .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal todayRevenue = orderRepository.findAll().stream()
            .filter(o -> o.getCreatedAt().isAfter(todayStart))
            .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pendingOrders = orderRepository.findAll().stream()
            .filter(o -> o.getStatus() == OrderStatus.PENDING_PAYMENT)
            .count();

        long lowStockProducts = productRepository.findAll().stream()
            .filter(p -> p.getStock() < 10 && p.getStock() > 0)
            .count();

        return AnalyticsDashboard.builder()
            .totalProducts(productRepository.count())
            .totalOrders(orderRepository.count())
            .totalUsers(userRepository.count())
            .totalRevenue(totalRevenue)
            .todayRevenue(todayRevenue)
            .pendingOrders(pendingOrders)
            .lowStockProducts(lowStockProducts)
            .salesTrend(getSalesTrend(7))
            .topProducts(getTopProducts(5))
            .orderStatusDistribution(getOrderStatusDistribution())
            .salesByCategory(getSalesByCategory())
            .build();
    }

    @Override
    public List<AnalyticsDashboard.SalesData> getSalesTrend(int days) {
        List<AnalyticsDashboard.SalesData> result = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

            BigDecimal revenue = orderRepository.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(dayStart) && o.getCreatedAt().isBefore(dayEnd))
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            long orderCount = orderRepository.findAll().stream()
                .filter(o -> o.getCreatedAt().isAfter(dayStart) && o.getCreatedAt().isBefore(dayEnd))
                .count();

            result.add(AnalyticsDashboard.SalesData.builder()
                .date(date.format(formatter))
                .revenue(revenue)
                .orderCount(orderCount)
                .build());
        }

        return result;
    }

    @Override
    public List<AnalyticsDashboard.ProductSales> getTopProducts(int limit) {
        Map<Long, ProductSalesAccumulator> productSalesMap = new HashMap<>();

        for (Order order : orderRepository.findAll()) {
            if (order.getStatus() == OrderStatus.CANCELLED) continue;

            for (var item : order.getOrderItems()) {
                productSalesMap.computeIfAbsent(item.getProductId(), k -> new ProductSalesAccumulator(item.getProductId(), item.getProductName()))
                    .addSales(item.getQuantity(), item.getSubtotal());
            }
        }

        return productSalesMap.values().stream()
            .sorted(Comparator.comparing(ProductSalesAccumulator::getRevenue).reversed())
            .limit(limit)
            .map(acc -> AnalyticsDashboard.ProductSales.builder()
                .productId(acc.productId)
                .productName(acc.productName)
                .salesCount(acc.salesCount)
                .revenue(acc.revenue)
                .build())
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getOrderStatusDistribution() {
        return orderRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                o -> o.getStatus().name(),
                Collectors.counting()
            ));
    }

    @Override
    public List<AnalyticsDashboard.CategorySales> getSalesByCategory() {
        // Simplified implementation
        return new ArrayList<>();
    }

    private static class ProductSalesAccumulator {
        private final Long productId;
        private final String productName;
        private Long salesCount = 0L;
        private BigDecimal revenue = BigDecimal.ZERO;

        ProductSalesAccumulator(Long productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }

        void addSales(int quantity, BigDecimal subtotal) {
            this.salesCount += quantity;
            this.revenue = this.revenue.add(subtotal);
        }

        Long getProductId() { return productId; }
        String getProductName() { return productName; }
        Long getSalesCount() { return salesCount; }
        BigDecimal getRevenue() { return revenue; }
    }
}
