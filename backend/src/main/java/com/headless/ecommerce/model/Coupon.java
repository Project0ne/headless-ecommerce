package com.headless.ecommerce.model;

import com.headless.ecommerce.model.enums.CouponType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Coupon entity for promotions and discounts.
 */
@Entity
@Table(name = "t_coupon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CouponType type;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "min_order_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count", nullable = false)
    @Builder.Default
    private Integer usedCount = 0;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Check if coupon is currently valid.
     */
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return active
            && now.isAfter(startDate)
            && now.isBefore(endDate)
            && (usageLimit == null || usedCount < usageLimit);
    }

    /**
     * Calculate discount amount for given order total.
     */
    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!isValid() || orderAmount.compareTo(minOrderAmount) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if (type == CouponType.PERCENTAGE) {
            discount = orderAmount.multiply(value).divide(BigDecimal.valueOf(100));
        } else {
            discount = value;
        }

        if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
            discount = maxDiscount;
        }

        return discount.min(orderAmount);
    }
}
