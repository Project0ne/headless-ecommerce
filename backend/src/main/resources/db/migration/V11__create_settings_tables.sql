-- Store Config
CREATE TABLE IF NOT EXISTS t_store_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    store_name VARCHAR(100) NOT NULL DEFAULT 'My Store',
    store_logo VARCHAR(500),
    store_description TEXT,
    contact_email VARCHAR(100),
    contact_phone VARCHAR(50),
    contact_address VARCHAR(500),
    timezone VARCHAR(50) NOT NULL DEFAULT 'Asia/Shanghai',
    currency_code VARCHAR(10) NOT NULL DEFAULT 'CNY',
    currency_symbol VARCHAR(10) NOT NULL DEFAULT '¥',
    language VARCHAR(10) NOT NULL DEFAULT 'zh-CN',
    custom_domain VARCHAR(200),
    favicon_url VARCHAR(500),
    footer_text TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO t_store_config (store_name, contact_email) VALUES ('E-Shop', 'admin@example.com');

-- Payment Methods
CREATE TABLE IF NOT EXISTS t_payment_method (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    icon_url VARCHAR(255),
    description TEXT,
    is_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INT NOT NULL DEFAULT 0,
    min_amount DECIMAL(10,2),
    max_amount DECIMAL(10,2),
    config_json TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO t_payment_method (name, type, is_enabled, sort_order) VALUES
('支付宝', 'ALIPAY', TRUE, 1),
('微信支付', 'WECHAT_PAY', TRUE, 2),
('信用卡', 'CREDIT_CARD', FALSE, 3),
('PayPal', 'PAYPAL', FALSE, 4),
('银行转账', 'BANK_TRANSFER', FALSE, 5),
('货到付款', 'CASH_ON_DELIVERY', FALSE, 6);

-- Shipping Methods
CREATE TABLE IF NOT EXISTS t_shipping_method (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(255),
    base_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    free_shipping_threshold DECIMAL(10,2),
    estimated_days_min INT,
    estimated_days_max INT,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INT NOT NULL DEFAULT 0,
    tracking_url_template VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO t_shipping_method (name, base_fee, free_shipping_threshold, estimated_days_min, estimated_days_max, is_enabled, sort_order) VALUES
('标准快递', 10.00, 99.00, 3, 7, TRUE, 1),
('顺丰速运', 22.00, 199.00, 1, 3, TRUE, 2),
('次日达', 35.00, NULL, 1, 1, FALSE, 3),
('自提', 0.00, NULL, 0, 0, TRUE, 4);

-- Tax Rates
CREATE TABLE IF NOT EXISTS t_tax_rate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    country_code VARCHAR(10) NOT NULL,
    state_code VARCHAR(50),
    city_code VARCHAR(50),
    postal_code VARCHAR(20),
    tax_rate DECIMAL(5,2) NOT NULL,
    is_compound BOOLEAN NOT NULL DEFAULT FALSE,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    priority INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO t_tax_rate (name, country_code, tax_rate, priority) VALUES
('中国大陆增值税', 'CN', 13.00, 1),
('美国销售税', 'US', 8.25, 1),
('欧盟增值税', 'EU', 20.00, 1);

-- Currencies
CREATE TABLE IF NOT EXISTS t_currency (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL,
    symbol VARCHAR(10) NOT NULL,
    exchange_rate DECIMAL(12,6) NOT NULL DEFAULT 1.000000,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    decimal_places INT NOT NULL DEFAULT 2,
    symbol_position VARCHAR(20) NOT NULL DEFAULT 'before',
    thousands_separator VARCHAR(5) NOT NULL DEFAULT ',',
    decimal_separator VARCHAR(5) NOT NULL DEFAULT '.',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO t_currency (code, name, symbol, is_default, is_enabled) VALUES
('CNY', '人民币', '¥', TRUE, TRUE),
('USD', '美元', '$', FALSE, TRUE),
('EUR', '欧元', '€', FALSE, TRUE),
('GBP', '英镑', '£', FALSE, TRUE),
('JPY', '日元', '¥', FALSE, TRUE),
('HKD', '港币', 'HK$', FALSE, TRUE);

-- Policies
CREATE TABLE IF NOT EXISTS t_policy (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    policy_type VARCHAR(50) NOT NULL,
    content LONGTEXT,
    is_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    is_required BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INT NOT NULL DEFAULT 0,
    version VARCHAR(20),
    effective_date DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO t_policy (title, policy_type, is_required, sort_order) VALUES
('隐私政策', 'PRIVACY', TRUE, 1),
('退款政策', 'REFUND', TRUE, 2),
('服务条款', 'TERMS', TRUE, 3),
('配送政策', 'SHIPPING', FALSE, 4);

-- Staff
CREATE TABLE IF NOT EXISTS t_staff (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(50),
    role VARCHAR(20) NOT NULL DEFAULT 'VIEWER',
    avatar_url VARCHAR(500),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at DATETIME,
    last_login_ip VARCHAR(50),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Audit Log
CREATE TABLE IF NOT EXISTS t_audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    operator_type VARCHAR(20) NOT NULL,
    operator_id BIGINT,
    operator_name VARCHAR(100),
    action VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50),
    entity_id BIGINT,
    entity_name VARCHAR(200),
    details TEXT,
    ip_address VARCHAR(50),
    user_agent VARCHAR(500),
    request_url VARCHAR(500),
    request_method VARCHAR(10),
    status_code INT,
    duration_ms BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_operator (operator_type, operator_id),
    INDEX idx_action (action),
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Media
CREATE TABLE IF NOT EXISTS t_media (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    original_name VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_type VARCHAR(50) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    width INT,
    height INT,
    alt_text VARCHAR(255),
    folder VARCHAR(100) NOT NULL DEFAULT 'default',
    uploaded_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_folder (folder),
    INDEX idx_file_type (file_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
