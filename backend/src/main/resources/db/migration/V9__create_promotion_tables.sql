-- Promotion table
CREATE TABLE t_promotion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    type VARCHAR(20) NOT NULL,
    discount_type VARCHAR(20),
    discount_value DECIMAL(10,2),
    min_purchase_amount DECIMAL(10,2),
    max_discount_amount DECIMAL(10,2),
    buy_quantity INT,
    get_quantity INT,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    priority INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_promotion_active ON t_promotion(active, start_date, end_date);
CREATE INDEX idx_promotion_type ON t_promotion(type);

-- Product-Promotion junction table
CREATE TABLE t_product_promotion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    promotion_id BIGINT NOT NULL,
    custom_price DECIMAL(10,2),
    FOREIGN KEY (product_id) REFERENCES t_product(id) ON DELETE CASCADE,
    FOREIGN KEY (promotion_id) REFERENCES t_promotion(id) ON DELETE CASCADE,
    UNIQUE KEY uk_product_promotion (product_id, promotion_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
