package com.headless.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Currency entity - multi-currency support.
 */
@Entity
@Table(name = "t_currency")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "symbol", nullable = false, length = 10)
    private String symbol;

    @Column(name = "exchange_rate", nullable = false, precision = 12, scale = 6)
    @Builder.Default
    private BigDecimal exchangeRate = BigDecimal.ONE;

    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    @Column(name = "is_enabled", nullable = false)
    @Builder.Default
    private Boolean isEnabled = true;

    @Column(name = "decimal_places", nullable = false)
    @Builder.Default
    private Integer decimalPlaces = 2;

    @Column(name = "symbol_position", length = 20)
    @Builder.Default
    private String symbolPosition = "before";

    @Column(name = "thousands_separator", length = 5)
    @Builder.Default
    private String thousandsSeparator = ",";

    @Column(name = "decimal_separator", length = 5)
    @Builder.Default
    private String decimalSeparator = ".";

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
