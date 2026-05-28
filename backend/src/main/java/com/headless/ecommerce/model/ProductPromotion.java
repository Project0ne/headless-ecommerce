package com.headless.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Many-to-many relationship between Product and Promotion.
 */
@Entity
@Table(name = "t_product_promotion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPromotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Column(name = "custom_price", precision = 10, scale = 2)
    private java.math.BigDecimal customPrice;
}
