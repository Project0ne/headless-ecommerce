package com.headless.ecommerce.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Store configuration entity - global store settings.
 */
@Entity
@Table(name = "t_store_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name", nullable = false, length = 100)
    @Builder.Default
    private String storeName = "My Store";

    @Column(name = "store_logo", length = 500)
    private String storeLogo;

    @Column(name = "store_description", columnDefinition = "TEXT")
    private String storeDescription;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "contact_address", length = 500)
    private String contactAddress;

    @Column(name = "timezone", nullable = false, length = 50)
    @Builder.Default
    private String timezone = "Asia/Shanghai";

    @Column(name = "currency_code", nullable = false, length = 10)
    @Builder.Default
    private String currencyCode = "CNY";

    @Column(name = "currency_symbol", nullable = false, length = 10)
    @Builder.Default
    private String currencySymbol = "¥";

    @Column(name = "language", nullable = false, length = 10)
    @Builder.Default
    private String language = "zh-CN";

    @Column(name = "custom_domain", length = 200)
    private String customDomain;

    @Column(name = "favicon_url", length = 500)
    private String faviconUrl;

    @Column(name = "footer_text", columnDefinition = "TEXT")
    private String footerText;

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
