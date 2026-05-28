package com.headless.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Tax rate entity - tax configuration by region.
 */
@Entity
@Table(name = "t_tax_rate")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "country_code", nullable = false, length = 10)
    private String countryCode;

    @Column(name = "state_code", length = 50)
    private String stateCode;

    @Column(name = "city_code", length = 50)
    private String cityCode;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "is_compound", nullable = false)
    @Builder.Default
    private Boolean isCompound = false;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = true;

    @Column(name = "priority", nullable = false)
    @Builder.Default
    private Integer priority = 0;

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
