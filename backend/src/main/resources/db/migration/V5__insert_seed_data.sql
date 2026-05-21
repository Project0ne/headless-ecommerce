-- Insert admin user (password: admin123, BCrypt encoded)
INSERT INTO t_user (username, password, nickname, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrator', 'ADMIN');

-- Insert categories
INSERT INTO t_category (name, icon, sort_order, parent_id) VALUES
('Electronics', '💻', 1, NULL),
('Clothing', '👕', 2, NULL),
('Home & Kitchen', '🏠', 3, NULL);

-- Insert products
INSERT INTO t_product (name, description, price, stock, image_url, status, sales_count, category_id) VALUES
('Smartphone X', 'Latest smartphone with advanced features', 6999.00, 100, '/images/placeholder-product.png', 'ON_SHELF', 56, 1),
('Wireless Earbuds', 'Bluetooth 5.0 noise-cancelling earbuds', 399.00, 200, '/images/placeholder-product.png', 'ON_SHELF', 120, 1),
('Laptop Pro', '15-inch laptop with high performance', 8999.00, 50, '/images/placeholder-product.png', 'ON_SHELF', 30, 1),
('Cotton T-Shirt', 'Premium cotton casual t-shirt', 129.00, 500, '/images/placeholder-product.png', 'ON_SHELF', 230, 2),
('Denim Jacket', 'Classic denim jacket for all seasons', 459.00, 80, '/images/placeholder-product.png', 'ON_SHELF', 67, 2),
('Coffee Maker', 'Automatic drip coffee maker', 599.00, 150, '/images/placeholder-product.png', 'ON_SHELF', 89, 3);
