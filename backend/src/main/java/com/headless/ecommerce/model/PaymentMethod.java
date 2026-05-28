package com.headless.ecommerce.model;

import com.headless.ecommerce.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Payment method entity - payment gateway configuration.
 */
@Entity
@Table(name = "t_payment_method")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentType type;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = false;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "min_amount", precision = 10, scale = 2)
    private java.math.BigDecimal minAmount;

    @Column(name = "max_amount", precision = 10, scale = 2)
    private java.math.BigDecimal maxAmount;

    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson;

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
