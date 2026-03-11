--liquibase formatted sql
-- Это самый первый рабочий файл
-- ==================== INSERT DEFAULT DATA ====================
--changeset tyzhprogramist:20
-- Insert default admin user (password: admin123 - нужно захешировать!)
INSERT INTO users (username, email, password, first_name, last_name, role, date_joined, notifications, is_active, email_verified)
VALUES ('admin', 'admin@example.com', '$2a$10$YourHashedPasswordHere', 'Admin', 'User', 'ADMIN', NOW(), TRUE, TRUE, FALSE);

-- Insert default moderator user (password: moderator123 - нужно захешировать!)
INSERT INTO users (username, email, password, first_name, last_name, role, date_joined, notifications, is_active, email_verified)
VALUES ('moderator', 'moderator@example.com', '$2a$10$YourHashedPasswordHere', 'Moderator', 'User', 'MODERATOR', NOW(), TRUE, TRUE, FALSE);

-- Insert default site settings
INSERT INTO site_settings (pickup_address, pickup_phone, pickup_working_hours, delivery_cost, free_delivery_threshold)
VALUES ('г. Москва, ул. Примерная, д. 1', '+7 (999) 123-45-67', 'Пн-Пт 10:00-20:00, Сб-Вс 11:00-18:00', 500.00, 5000.00);

-- Insert default categories
INSERT INTO categories (name, slug, sort_order) VALUES
('Процессоры', 'processory', 1),
('Материнские платы', 'motherboards', 2),
('Видеокарты', 'videocards', 3),
('Оперативная память', 'ram', 4),
('Накопители', 'storage', 5),
('Блоки питания', 'power-supplies', 6),
('Корпуса', 'cases', 7),
('Охлаждение', 'cooling', 8);

-- Insert default component types
INSERT INTO component_types (name, order_step) VALUES
('Процессор', 1),
('Материнская плата', 2),
('Оперативная память', 3),
('Видеокарта', 4),
('Накопитель', 5),
('Блок питания', 6),
('Корпус', 7),
('Охлаждение', 8);

--changeset tyzhprogramist:21
-- Insert sample products (if needed)
-- Процессоры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-001', id, 'Intel Core i5-12400F', 'intel-core-i5-12400f', 'Процессор Intel Core i5-12400F, LGA 1700, 6 ядер', 15000.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-002', id, 'AMD Ryzen 5 5600X', 'amd-ryzen-5-5600x', 'Процессор AMD Ryzen 5 5600X, AM4, 6 ядер', 18000.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

-- Видеокарты
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-001', id, 'NVIDIA RTX 3060', 'nvidia-rtx-3060', 'Видеокарта NVIDIA GeForce RTX 3060 12GB', 35000.00, 5, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-002', id, 'AMD Radeon RX 6600', 'amd-rx-6600', 'Видеокарта AMD Radeon RX 6600 8GB', 30000.00, 5, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

-- Оперативная память
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-001', id, 'Kingston Fury 16GB', 'kingston-fury-16gb', 'Оперативная память Kingston Fury 16GB DDR4', 6000.00, 20, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'ram';

-- Накопители
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-001', id, 'Samsung 980 1TB', 'samsung-980-1tb', 'SSD накопитель Samsung 980 1TB NVMe', 9000.00, 15, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

--changeset tyzhprogramist:22
-- Insert related products
INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'GPU-001';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-002' AND p2.sku = 'GPU-002';

-- Insert frequently bought together
INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'RAM-001';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'SSD-001';