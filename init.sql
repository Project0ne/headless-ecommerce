-- Headless E-Commerce Database Initialization
-- This file is automatically executed when MySQL starts for the first time

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS ecommerce
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE ecommerce;

-- Create default admin user (password: admin123, will be hashed by Spring Boot)
-- The actual tables will be created by Flyway migrations
-- This is just for reference, the app will auto-create tables on first run

-- Default admin credentials:
-- Username: admin
-- Password: admin123 (BCrypt hashed: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9tYjKUiFjFO2/fG)
