
-- ShopSphere E-Commerce System Database Schema & Seed Data
-- Designed for RTU Advanced Java / Java Enterprise Lab Capstone
-- =====================================================================

DROP DATABASE IF EXISTS shopsphere;
CREATE DATABASE shopsphere CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

-- 1. USERS TABLE
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- SHA-256 hashed
    mobile VARCHAR(20),
    role VARCHAR(20) DEFAULT 'CUSTOMER', -- 'CUSTOMER', 'ADMIN'
    status VARCHAR(20) DEFAULT 'ACTIVE', -- 'ACTIVE', 'INACTIVE'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. ADDRESSES TABLE
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    address_type VARCHAR(50) DEFAULT 'HOME',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. CATEGORIES TABLE
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- 4. PRODUCTS TABLE
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(5,2) DEFAULT 0.00,
    stock INT DEFAULT 0,
    image_url VARCHAR(500),
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- 5. CART TABLE
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_product (user_id, product_id)
);

-- 6. WISHLIST TABLE
CREATE TABLE wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_wishlist (user_id, product_id)
);

-- 7. ORDERS TABLE
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) DEFAULT 0.00,
    payment_method VARCHAR(50) DEFAULT 'COD',
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    order_status VARCHAR(50) DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE SET NULL
);

-- 8. ORDER ITEMS TABLE
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 9. PAYMENTS TABLE
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_reference VARCHAR(100),
    amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(50) DEFAULT 'COMPLETED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- 10. REVIEWS TABLE
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 11. COUPONS TABLE
CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_type VARCHAR(20) DEFAULT 'PERCENT', -- 'PERCENT' or 'FLAT'
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_order DECIMAL(10,2) DEFAULT 0.00,
    maximum_discount DECIMAL(10,2) DEFAULT 1000.00,
    expiry_date DATE,
    status VARCHAR(20) DEFAULT 'ACTIVE'
);

-- 12. COUPON USAGE TABLE
CREATE TABLE coupon_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- =====================================================================
-- SEED DATA
-- SHA-256 for 'admin123' = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- SHA-256 for 'user123'  = e6c279e8cd401f8d4826b68a8607973d4ee7f311c4708ff812976d29b0153835
-- =====================================================================

INSERT INTO users (user_id, name, email, password, mobile, role, status) VALUES
(1, 'Admin Officer', 'admin@shopsphere.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', '9876543210', 'ADMIN', 'ACTIVE'),
(2, 'Rahul Sharma', 'rahul@example.com', 'e6c279e8cd401f8d4826b68a8607973d4ee7f311c4708ff812976d29b0153835', '9822334455', 'CUSTOMER', 'ACTIVE'),
(3, 'John Doe', 'john@example.com', 'e6c279e8cd401f8d4826b68a8607973d4ee7f311c4708ff812976d29b0153835', '9811223344', 'CUSTOMER', 'ACTIVE'),
(4, 'Mehul Suthar', 'mehulsuthar304@gmail.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', '8850449086', 'ADMIN', 'ACTIVE');

INSERT INTO addresses (address_id, user_id, address_line, city, state, pincode, address_type) VALUES
(1, 2, 'Flat 402, Royal Residency, MI Road', 'Jaipur', 'Rajasthan', '302001', 'HOME'),
(2, 2, 'Tech Park Phase 2, Sitapura', 'Jaipur', 'Rajasthan', '302022', 'OFFICE'),
(3, 3, 'Plot 15, Green Avenue', 'Kota', 'Rajasthan', '324005', 'HOME');

INSERT INTO categories (category_id, name, description, status) VALUES
(1, 'Electronics', 'Laptops, Smartphones, Audio gear, and accessories', 'ACTIVE'),
(2, 'Fashion', 'Apparel, footwear, watches, and personal lifestyle', 'ACTIVE'),
(3, 'Home & Kitchen', 'Home appliances, decor, cookware, and essentials', 'ACTIVE'),
(4, 'Books & Media', 'Engineering textbooks, novels, and educational guides', 'ACTIVE'),
(5, 'Fitness & Sports', 'Sports gear, gym equipment, and wellness trackers', 'ACTIVE');

INSERT INTO products (product_id, category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
(1, 1, 'Sony WH-1000XM5 Wireless Headphones', 'Sony', 'Industry leading noise cancellation with two processors and 8 microphones. Up to 30 hours battery life.', 26990.00, 10.00, 25, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500', 'ACTIVE'),
(2, 1, 'Apple MacBook Air M3 (16GB, 512GB)', 'Apple', 'Strikingly thin and fast with 13.6-inch Liquid Retina Display, 18 hours battery life, MagSafe charging.', 114900.00, 5.00, 15, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500', 'ACTIVE'),
(3, 1, 'Samsung Galaxy S24 Ultra 5G', 'Samsung', '200MP camera, Snapdragon 8 Gen 3 for Galaxy, titanium frame, built-in S-Pen, and Galaxy AI.', 129999.00, 8.00, 20, 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=500', 'ACTIVE'),
(4, 1, 'Logitech MX Master 3S Wireless Mouse', 'Logitech', 'Quiet clicks, 8K DPI any-surface tracking, MagSpeed electromagnetic scrolling, ergonomic design.', 8995.00, 12.00, 40, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=500', 'ACTIVE'),
(5, 2, 'Levis Slim Fit Denim Jacket', 'Levis', 'Classic trucker jacket made from 100% premium cotton denim with shank buttons and chest pockets.', 3999.00, 25.00, 50, 'https://images.unsplash.com/photo-1576995853123-5a10305d93c0?w=500', 'ACTIVE'),
(6, 2, 'Fossil Gen 6 Smartwatch', 'Fossil', 'Stainless steel case with Wear OS by Google, SpO2 sensor, heart rate tracking, rapid charging.', 14995.00, 30.00, 18, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500', 'ACTIVE'),
(7, 3, 'Philips Air Fryer XL (4.2L)', 'Philips', 'Rapid Air technology for 90% less fat cooking, digital touchscreen with 7 preset cooking programs.', 7499.00, 20.00, 30, 'https://images.unsplash.com/photo-1586208958839-06c17cacdf08?w=500', 'ACTIVE'),
(8, 3, 'Nespresso Essenza Mini Coffee Machine', 'Nespresso', 'Ultra-compact espresso maker with 19-bar high pressure pump, 2 programmable cup sizes.', 11999.00, 15.00, 12, 'https://images.unsplash.com/photo-1517668808822-9ebb02ae2a0e?w=500', 'ACTIVE'),
(9, 4, 'Core Java for Advanced Developers', 'Oracle Press', 'Comprehensive volume detailing Jakarta EE, Servlets, JSP, JDBC, RMI, Sockets and Multithreading.', 1299.00, 15.00, 60, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500', 'ACTIVE'),
(10, 5, 'Bowflex SelectTech Adjustable Dumbbells', 'Bowflex', 'Rapid adjustment from 2.5kg to 24kg per dumbbell, space-efficient durable molding.', 18500.00, 10.00, 8, 'https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?w=500', 'ACTIVE');

INSERT INTO coupons (coupon_id, code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
(1, 'WELCOME10', 'PERCENT', 10.00, 500.00, 1000.00, '2027-12-31', 'ACTIVE'),
(2, 'FESTIVE500', 'FLAT', 500.00, 3000.00, 500.00, '2027-12-31', 'ACTIVE'),
(3, 'TECH20', 'PERCENT', 20.00, 5000.00, 2500.00, '2027-12-31', 'ACTIVE');

INSERT INTO reviews (review_id, user_id, product_id, rating, review_text) VALUES
(1, 2, 1, 5, 'Exceptional noise cancellation and great comfort during long coding sessions!'),
(2, 3, 2, 5, 'Super fast M3 processor and stellar battery life. Best machine for Java development.'),
(3, 2, 4, 4, 'Ergonomic mouse with smooth scrolling. Worth every rupee.');

INSERT INTO orders (order_id, user_id, address_id, total_amount, discount, payment_method, payment_status, order_status) VALUES
(101, 2, 1, 24291.00, 2699.00, 'CARD', 'COMPLETED', 'DELIVERED'),
(102, 3, 3, 8995.00, 0.00, 'UPI', 'COMPLETED', 'SHIPPED');

INSERT INTO order_items (order_item_id, order_id, product_id, quantity, price) VALUES
(1, 101, 1, 1, 24291.00),
(2, 102, 4, 1, 8995.00);

INSERT INTO payments (payment_id, order_id, payment_method, transaction_reference, amount, payment_status) VALUES
(1, 101, 'CARD', 'TXN_CARD_887291', 24291.00, 'COMPLETED'),
(2, 102, 'UPI', 'UPI_PAY_773019', 8995.00, 'COMPLETED');
