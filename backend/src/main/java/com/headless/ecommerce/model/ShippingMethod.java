package com.headless.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Shipping method entity - delivery options configuration.
 */
@Entity
@Table(name = "t_shipping_method")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;

    @Column(name = "base_fee", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal baseFee = BigDecimal.ZERO;

    @Column(name = "free_shipping_threshold", precision = 10, scale = 2)
    private BigDecimal freeShippingThreshold;

    @Column(name = "estimated_days_min")
    private Integer estimatedDaysMin;

    @Column(name = "estimated_days_max")
    private Integer estimatedDaysMax;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = true;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "tracking_url_template", length = 500)
    private String trackingUrlTemplate;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
