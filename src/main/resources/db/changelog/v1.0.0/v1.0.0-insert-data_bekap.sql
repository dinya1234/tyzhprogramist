--liquibase formatted sql

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
-- ==================== ПРОЦЕССОРЫ INTEL ====================
-- Intel Core i3
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-001', id, 'Intel Core i3-12100F', 'intel-core-i3-12100f', 'Процессор Intel Core i3-12100F, LGA 1700, 4 ядра, 8 потоков, до 4.3 ГГц', 8500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-002', id, 'Intel Core i3-13100', 'intel-core-i3-13100', 'Процессор Intel Core i3-13100, LGA 1700, 4 ядра, 8 потоков, до 4.5 ГГц, встроенная графика', 11500.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

-- Intel Core i5
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-003', id, 'Intel Core i5-12400F', 'intel-core-i5-12400f', 'Процессор Intel Core i5-12400F, LGA 1700, 6 ядер, 12 потоков, до 4.4 ГГц', 15000.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-004', id, 'Intel Core i5-12600K', 'intel-core-i5-12600k', 'Процессор Intel Core i5-12600K, LGA 1700, 10 ядер (6P+4E), 16 потоков, до 4.9 ГГц', 22000.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-005', id, 'Intel Core i5-13400F', 'intel-core-i5-13400f', 'Процессор Intel Core i5-13400F, LGA 1700, 10 ядер (6P+4E), 16 потоков, до 4.6 ГГц', 19000.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-006', id, 'Intel Core i5-13600K', 'intel-core-i5-13600k', 'Процессор Intel Core i5-13600K, LGA 1700, 14 ядер (6P+8E), 20 потоков, до 5.1 ГГц', 28500.00, 7, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

-- Intel Core i7
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-007', id, 'Intel Core i7-12700K', 'intel-core-i7-12700k', 'Процессор Intel Core i7-12700K, LGA 1700, 12 ядер (8P+4E), 20 потоков, до 5.0 ГГц', 29000.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-008', id, 'Intel Core i7-13700K', 'intel-core-i7-13700k', 'Процессор Intel Core i7-13700K, LGA 1700, 16 ядер (8P+8E), 24 потока, до 5.4 ГГц', 37500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-009', id, 'Intel Core i7-14700K', 'intel-core-i7-14700k', 'Процессор Intel Core i7-14700K, LGA 1700, 20 ядер (8P+12E), 28 потоков, до 5.6 ГГц', 42500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- Intel Core i9
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-010', id, 'Intel Core i9-12900K', 'intel-core-i9-12900k', 'Процессор Intel Core i9-12900K, LGA 1700, 16 ядер (8P+8E), 24 потока, до 5.2 ГГц', 45000.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-011', id, 'Intel Core i9-13900K', 'intel-core-i9-13900k', 'Процессор Intel Core i9-13900K, LGA 1700, 24 ядра (8P+16E), 32 потока, до 5.8 ГГц', 58000.00, 3, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-012', id, 'Intel Core i9-14900K', 'intel-core-i9-14900k', 'Процессор Intel Core i9-14900K, LGA 1700, 24 ядра (8P+16E), 32 потока, до 6.0 ГГц', 65000.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- ==================== ПРОЦЕССОРЫ AMD ====================
-- AMD Ryzen 3
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-001', id, 'AMD Ryzen 3 4100', 'amd-ryzen-3-4100', 'Процессор AMD Ryzen 3 4100, AM4, 4 ядра, 8 потоков, до 4.0 ГГц', 6500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-002', id, 'AMD Ryzen 3 5300G', 'amd-ryzen-3-5300g', 'Процессор AMD Ryzen 3 5300G, AM4, 4 ядра, 8 потоков, до 4.2 ГГц, встроенная графика', 9500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- AMD Ryzen 5
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-003', id, 'AMD Ryzen 5 4500', 'amd-ryzen-5-4500', 'Процессор AMD Ryzen 5 4500, AM4, 6 ядер, 12 потоков, до 4.1 ГГц', 9000.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-004', id, 'AMD Ryzen 5 5600X', 'amd-ryzen-5-5600x', 'Процессор AMD Ryzen 5 5600X, AM4, 6 ядер, 12 потоков, до 4.6 ГГц', 18000.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-005', id, 'AMD Ryzen 5 7500F', 'amd-ryzen-5-7500f', 'Процессор AMD Ryzen 5 7500F, AM5, 6 ядер, 12 потоков, до 5.0 ГГц', 16500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-006', id, 'AMD Ryzen 5 7600X', 'amd-ryzen-5-7600x', 'Процессор AMD Ryzen 5 7600X, AM5, 6 ядер, 12 потоков, до 5.3 ГГц', 20500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- AMD Ryzen 7
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-007', id, 'AMD Ryzen 7 5700X', 'amd-ryzen-7-5700x', 'Процессор AMD Ryzen 7 5700X, AM4, 8 ядер, 16 потоков, до 4.6 ГГц', 19000.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-008', id, 'AMD Ryzen 7 5800X3D', 'amd-ryzen-7-5800x3d', 'Процессор AMD Ryzen 7 5800X3D, AM4, 8 ядер, 16 потоков, до 4.5 ГГц, 3D V-Cache', 28000.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-009', id, 'AMD Ryzen 7 7700X', 'amd-ryzen-7-7700x', 'Процессор AMD Ryzen 7 7700X, AM5, 8 ядер, 16 потоков, до 5.4 ГГц', 26500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-010', id, 'AMD Ryzen 7 7800X3D', 'amd-ryzen-7-7800x3d', 'Процессор AMD Ryzen 7 7800X3D, AM5, 8 ядер, 16 потоков, до 5.0 ГГц, 3D V-Cache', 36500.00, 4, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

-- AMD Ryzen 9
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-011', id, 'AMD Ryzen 9 5900X', 'amd-ryzen-9-5900x', 'Процессор AMD Ryzen 9 5900X, AM4, 12 ядер, 24 потока, до 4.8 ГГц', 29500.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-012', id, 'AMD Ryzen 9 5950X', 'amd-ryzen-9-5950x', 'Процессор AMD Ryzen 9 5950X, AM4, 16 ядер, 32 потока, до 4.9 ГГц', 42500.00, 3, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-013', id, 'AMD Ryzen 9 7900X', 'amd-ryzen-9-7900x', 'Процессор AMD Ryzen 9 7900X, AM5, 12 ядер, 24 потока, до 5.6 ГГц', 39500.00, 3, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-014', id, 'AMD Ryzen 9 7950X3D', 'amd-ryzen-9-7950x3d', 'Процессор AMD Ryzen 9 7950X3D, AM5, 16 ядер, 32 потока, до 5.7 ГГц, 3D V-Cache', 59500.00, 2, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

-- ==================== ВИДЕОКАРТЫ NVIDIA ====================
-- RTX 30 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-001', id, 'NVIDIA RTX 3050', 'nvidia-rtx-3050', 'Видеокарта NVIDIA GeForce RTX 3050 8GB GDDR6', 23000.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-002', id, 'NVIDIA RTX 3060 12GB', 'nvidia-rtx-3060-12gb', 'Видеокарта NVIDIA GeForce RTX 3060 12GB GDDR6', 32000.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-003', id, 'NVIDIA RTX 3060 Ti', 'nvidia-rtx-3060-ti', 'Видеокарта NVIDIA GeForce RTX 3060 Ti 8GB GDDR6', 36000.00, 7, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-004', id, 'NVIDIA RTX 3070', 'nvidia-rtx-3070', 'Видеокарта NVIDIA GeForce RTX 3070 8GB GDDR6', 48000.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-005', id, 'NVIDIA RTX 3070 Ti', 'nvidia-rtx-3070-ti', 'Видеокарта NVIDIA GeForce RTX 3070 Ti 8GB GDDR6X', 52000.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-006', id, 'NVIDIA RTX 3080', 'nvidia-rtx-3080', 'Видеокарта NVIDIA GeForce RTX 3080 10GB GDDR6X', 65000.00, 3, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-007', id, 'NVIDIA RTX 3080 Ti', 'nvidia-rtx-3080-ti', 'Видеокарта NVIDIA GeForce RTX 3080 Ti 12GB GDDR6X', 78000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-008', id, 'NVIDIA RTX 3090', 'nvidia-rtx-3090', 'Видеокарта NVIDIA GeForce RTX 3090 24GB GDDR6X', 115000.00, 1, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-009', id, 'NVIDIA RTX 3090 Ti', 'nvidia-rtx-3090-ti', 'Видеокарта NVIDIA GeForce RTX 3090 Ti 24GB GDDR6X', 145000.00, 1, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

-- RTX 40 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-010', id, 'NVIDIA RTX 4060', 'nvidia-rtx-4060', 'Видеокарта NVIDIA GeForce RTX 4060 8GB GDDR6', 29500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-011', id, 'NVIDIA RTX 4060 Ti 8GB', 'nvidia-rtx-4060-ti-8gb', 'Видеокарта NVIDIA GeForce RTX 4060 Ti 8GB GDDR6', 37500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-012', id, 'NVIDIA RTX 4060 Ti 16GB', 'nvidia-rtx-4060-ti-16gb', 'Видеокарта NVIDIA GeForce RTX 4060 Ti 16GB GDDR6', 44500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-013', id, 'NVIDIA RTX 4070', 'nvidia-rtx-4070', 'Видеокарта NVIDIA GeForce RTX 4070 12GB GDDR6X', 56000.00, 7, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-014', id, 'NVIDIA RTX 4070 SUPER', 'nvidia-rtx-4070-super', 'Видеокарта NVIDIA GeForce RTX 4070 SUPER 12GB GDDR6X', 62500.00, 5, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-015', id, 'NVIDIA RTX 4070 Ti', 'nvidia-rtx-4070-ti', 'Видеокарта NVIDIA GeForce RTX 4070 Ti 12GB GDDR6X', 73000.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-016', id, 'NVIDIA RTX 4070 Ti SUPER', 'nvidia-rtx-4070-ti-super', 'Видеокарта NVIDIA GeForce RTX 4070 Ti SUPER 16GB GDDR6X', 82500.00, 3, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-017', id, 'NVIDIA RTX 4080', 'nvidia-rtx-4080', 'Видеокарта NVIDIA GeForce RTX 4080 16GB GDDR6X', 105000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-018', id, 'NVIDIA RTX 4080 SUPER', 'nvidia-rtx-4080-super', 'Видеокарта NVIDIA GeForce RTX 4080 SUPER 16GB GDDR6X', 115000.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-019', id, 'NVIDIA RTX 4090', 'nvidia-rtx-4090', 'Видеокарта NVIDIA GeForce RTX 4090 24GB GDDR6X', 165000.00, 1, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

-- ==================== ВИДЕОКАРТЫ AMD ====================
-- RX 6000 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-001', id, 'AMD Radeon RX 6400', 'amd-rx-6400', 'Видеокарта AMD Radeon RX 6400 4GB GDDR6', 12000.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-002', id, 'AMD Radeon RX 6500 XT', 'amd-rx-6500-xt', 'Видеокарта AMD Radeon RX 6500 XT 4GB GDDR6', 15500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-003', id, 'AMD Radeon RX 6600', 'amd-rx-6600', 'Видеокарта AMD Radeon RX 6600 8GB GDDR6', 24000.00, 12, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-004', id, 'AMD Radeon RX 6600 XT', 'amd-rx-6600-xt', 'Видеокарта AMD Radeon RX 6600 XT 8GB GDDR6', 28000.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-005', id, 'AMD Radeon RX 6650 XT', 'amd-rx-6650-xt', 'Видеокарта AMD Radeon RX 6650 XT 8GB GDDR6', 29500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-006', id, 'AMD Radeon RX 6700 XT', 'amd-rx-6700-xt', 'Видеокарта AMD Radeon RX 6700 XT 12GB GDDR6', 33500.00, 7, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-007', id, 'AMD Radeon RX 6750 XT', 'amd-rx-6750-xt', 'Видеокарта AMD Radeon RX 6750 XT 12GB GDDR6', 36500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-008', id, 'AMD Radeon RX 6800', 'amd-rx-6800', 'Видеокарта AMD Radeon RX 6800 16GB GDDR6', 45000.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-009', id, 'AMD Radeon RX 6800 XT', 'amd-rx-6800-xt', 'Видеокарта AMD Radeon RX 6800 XT 16GB GDDR6', 52000.00, 3, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-010', id, 'AMD Radeon RX 6900 XT', 'amd-rx-6900-xt', 'Видеокарта AMD Radeon RX 6900 XT 16GB GDDR6', 65000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-011', id, 'AMD Radeon RX 6950 XT', 'amd-rx-6950-xt', 'Видеокарта AMD Radeon RX 6950 XT 16GB GDDR6', 72000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

-- RX 7000 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-012', id, 'AMD Radeon RX 7600', 'amd-rx-7600', 'Видеокарта AMD Radeon RX 7600 8GB GDDR6', 26500.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-013', id, 'AMD Radeon RX 7600 XT', 'amd-rx-7600-xt', 'Видеокарта AMD Radeon RX 7600 XT 16GB GDDR6', 32500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-014', id, 'AMD Radeon RX 7700 XT', 'amd-rx-7700-xt', 'Видеокарта AMD Radeon RX 7700 XT 12GB GDDR6', 42500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-015', id, 'AMD Radeon RX 7800 XT', 'amd-rx-7800-xt', 'Видеокарта AMD Radeon RX 7800 XT 16GB GDDR6', 52500.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-016', id, 'AMD Radeon RX 7900 GRE', 'amd-rx-7900-gre', 'Видеокарта AMD Radeon RX 7900 GRE 16GB GDDR6', 59500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-017', id, 'AMD Radeon RX 7900 XT', 'amd-rx-7900-xt', 'Видеокарта AMD Radeon RX 7900 XT 20GB GDDR6', 75000.00, 3, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-018', id, 'AMD Radeon RX 7900 XTX', 'amd-rx-7900-xtx', 'Видеокарта AMD Radeon RX 7900 XTX 24GB GDDR6', 92500.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

-- ==================== МАТЕРИНСКИЕ ПЛАТЫ ====================
-- Socket AM4
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-001', id, 'MSI B550-A PRO', 'msi-b550-a-pro', 'Материнская плата MSI B550-A PRO, Socket AM4, ATX', 10500.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-002', id, 'ASUS TUF GAMING B550-PLUS', 'asus-tuf-b550-plus', 'Материнская плата ASUS TUF GAMING B550-PLUS, Socket AM4, ATX', 12000.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-003', id, 'Gigabyte B550 AORUS ELITE', 'gigabyte-b550-aorus-elite', 'Материнская плата Gigabyte B550 AORUS ELITE V2, Socket AM4, ATX', 11500.00, 7, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-004', id, 'ASRock B550 Steel Legend', 'asrock-b550-steel-legend', 'Материнская плата ASRock B550 Steel Legend, Socket AM4, ATX', 13000.00, 5, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-005', id, 'MSI MAG B550 TOMAHAWK', 'msi-mag-b550-tomahawk', 'Материнская плата MSI MAG B550 TOMAHAWK, Socket AM4, ATX', 14000.00, 5, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-006', id, 'ASUS ROG STRIX B550-F', 'asus-rog-strix-b550-f', 'Материнская плата ASUS ROG STRIX B550-F GAMING, Socket AM4, ATX', 15500.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-007', id, 'MSI X570-A PRO', 'msi-x570-a-pro', 'Материнская плата MSI X570-A PRO, Socket AM4, ATX', 13500.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-008', id, 'ASUS ROG CROSSHAIR VIII DARK HERO', 'asus-crosshair-viii-dark-hero', 'Материнская плата ASUS ROG CROSSHAIR VIII DARK HERO, Socket AM4, ATX', 32500.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

-- Socket AM5
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-009', id, 'MSI B650 GAMING PLUS', 'msi-b650-gaming-plus', 'Материнская плата MSI B650 GAMING PLUS, Socket AM5, ATX', 15500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-010', id, 'ASUS TUF GAMING B650-PLUS', 'asus-tuf-b650-plus', 'Материнская плата ASUS TUF GAMING B650-PLUS, Socket AM5, ATX', 17000.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-011', id, 'Gigabyte B650 AORUS ELITE', 'gigabyte-b650-aorus-elite', 'Материнская плата Gigabyte B650 AORUS ELITE, Socket AM5, ATX', 17500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-012', id, 'ASRock B650 Steel Legend', 'asrock-b650-steel-legend', 'Материнская плата ASRock B650 Steel Legend, Socket AM5, ATX', 18500.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-013', id, 'MSI MPG B650 CARBON', 'msi-mpg-b650-carbon', 'Материнская плата MSI MPG B650 CARBON WIFI, Socket AM5, ATX', 24500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-014', id, 'ASUS ROG STRIX B650E-F', 'asus-rog-strix-b650e-f', 'Материнская плата ASUS ROG STRIX B650E-F GAMING WIFI, Socket AM5, ATX', 22500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-015', id, 'MSI X670E CARBON', 'msi-x670e-carbon', 'Материнская плата MSI MPG X670E CARBON WIFI, Socket AM5, ATX', 34500.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-016', id, 'ASUS ROG CROSSHAIR X670E HERO', 'asus-crosshair-x670e-hero', 'Материнская плата ASUS ROG CROSSHAIR X670E HERO, Socket AM5, ATX', 52500.00, 2, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

-- Socket LGA1700
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-017', id, 'MSI PRO B660-A', 'msi-pro-b660-a', 'Материнская плата MSI PRO B660-A, LGA 1700, ATX', 9500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-018', id, 'Gigabyte B760 DS3H', 'gigabyte-b760-ds3h', 'Материнская плата Gigabyte B760 DS3H, LGA 1700, ATX', 10500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-019', id, 'MSI B760 GAMING PLUS', 'msi-b760-gaming-plus', 'Материнская плата MSI B760 GAMING PLUS WIFI, LGA 1700, ATX', 13500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-020', id, 'ASUS TUF GAMING B760-PLUS', 'asus-tuf-b760-plus', 'Материнская плата ASUS TUF GAMING B760-PLUS WIFI, LGA 1700, ATX', 15500.00, 7, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-021', id, 'MSI Z790 GAMING', 'msi-z790-gaming', 'Материнская плата MSI Z790 GAMING PLUS WIFI, LGA 1700, ATX', 18500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-022', id, 'ASUS ROG STRIX Z790-E', 'asus-rog-strix-z790-e', 'Материнская плата ASUS ROG STRIX Z790-E GAMING WIFI, LGA 1700, ATX', 28500.00, 4, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-023', id, 'Gigabyte Z790 AORUS MASTER', 'gigabyte-z790-aorus-master', 'Материнская плата Gigabyte Z790 AORUS MASTER, LGA 1700, ATX', 37500.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-024', id, 'MSI MEG Z790 ACE', 'msi-meg-z790-ace', 'Материнская плата MSI MEG Z790 ACE, LGA 1700, ATX', 46500.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

-- ==================== ОПЕРАТИВНАЯ ПАМЯТЬ ====================
-- DDR4
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-001', id, 'Kingston Fury 8GB DDR4', 'kingston-fury-8gb-ddr4', 'Оперативная память Kingston Fury 8GB DDR4 3200MHz', 2800.00, 30, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-002', id, 'Kingston Fury 16GB DDR4', 'kingston-fury-16gb-ddr4', 'Оперативная память Kingston Fury 16GB DDR4 3200MHz', 5200.00, 25, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-003', id, 'Kingston Fury 32GB DDR4', 'kingston-fury-32gb-ddr4', 'Оперативная память Kingston Fury 32GB DDR4 3200MHz', 9800.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-004', id, 'Corsair Vengeance LPX 16GB DDR4', 'corsair-vengeance-16gb-ddr4', 'Оперативная память Corsair Vengeance LPX 16GB DDR4 3200MHz', 5600.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-005', id, 'Corsair Vengeance RGB 16GB DDR4', 'corsair-vengeance-rgb-16gb-ddr4', 'Оперативная память Corsair Vengeance RGB 16GB DDR4 3600MHz', 6800.00, 18, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-006', id, 'G.Skill Trident Z 16GB DDR4', 'gskill-trident-16gb-ddr4', 'Оперативная память G.Skill Trident Z RGB 16GB DDR4 3600MHz', 7200.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-007', id, 'G.Skill Trident Z 32GB DDR4', 'gskill-trident-32gb-ddr4', 'Оперативная память G.Skill Trident Z RGB 32GB DDR4 3600MHz', 13500.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-008', id, 'Samsung 8GB DDR4', 'samsung-8gb-ddr4', 'Оперативная память Samsung 8GB DDR4 2666MHz', 2100.00, 35, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-009', id, 'Samsung 16GB DDR4', 'samsung-16gb-ddr4', 'Оперативная память Samsung 16GB DDR4 2666MHz', 4000.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-010', id, 'Team Group T-Force 32GB DDR4', 'teamgroup-tforce-32gb-ddr4', 'Оперативная память Team Group T-Force Delta RGB 32GB DDR4 3600MHz', 11500.00, 12, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'ram';

-- DDR5
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-001', id, 'Kingston Fury 16GB DDR5', 'kingston-fury-16gb-ddr5', 'Оперативная память Kingston Fury 16GB DDR5 5200MHz', 6200.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-002', id, 'Kingston Fury 32GB DDR5', 'kingston-fury-32gb-ddr5', 'Оперативная память Kingston Fury 32GB DDR5 5600MHz', 11500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-003', id, 'Kingston Fury 64GB DDR5', 'kingston-fury-64gb-ddr5', 'Оперативная память Kingston Fury 64GB DDR5 5200MHz', 21500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-004', id, 'Corsair Vengeance 16GB DDR5', 'corsair-vengeance-16gb-ddr5', 'Оперативная память Corsair Vengeance 16GB DDR5 5600MHz', 6800.00, 18, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-005', id, 'Corsair Vengeance 32GB DDR5', 'corsair-vengeance-32gb-ddr5', 'Оперативная память Corsair Vengeance 32GB DDR5 6000MHz', 12500.00, 12, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-006', id, 'G.Skill Trident Z5 32GB DDR5', 'gskill-trident-z5-32gb-ddr5', 'Оперативная память G.Skill Trident Z5 RGB 32GB DDR5 6000MHz', 14500.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-007', id, 'G.Skill Trident Z5 64GB DDR5', 'gskill-trident-z5-64gb-ddr5', 'Оперативная память G.Skill Trident Z5 RGB 64GB DDR5 6000MHz', 26500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-008', id, 'Team Group T-Force 32GB DDR5', 'teamgroup-tforce-32gb-ddr5', 'Оперативная память Team Group T-Force Delta RGB 32GB DDR5 6000MHz', 13500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-009', id, 'ADATA XPG Lancer 16GB DDR5', 'adata-xpg-lancer-16gb-ddr5', 'Оперативная память ADATA XPG Lancer 16GB DDR5 5200MHz', 5800.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'ram';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-010', id, 'ADATA XPG Lancer 32GB DDR5', 'adata-xpg-lancer-32gb-ddr5', 'Оперативная память ADATA XPG Lancer 32GB DDR5 6000MHz', 11500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'ram';

-- ==================== НАКОПИТЕЛИ SSD ====================
-- SATA SSD
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-001', id, 'Samsung 870 EVO 250GB', 'samsung-870-evo-250gb', 'SSD накопитель Samsung 870 EVO 250GB SATA III', 3500.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-002', id, 'Samsung 870 EVO 500GB', 'samsung-870-evo-500gb', 'SSD накопитель Samsung 870 EVO 500GB SATA III', 5500.00, 30, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-003', id, 'Samsung 870 EVO 1TB', 'samsung-870-evo-1tb', 'SSD накопитель Samsung 870 EVO 1TB SATA III', 9500.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-004', id, 'Samsung 870 QVO 2TB', 'samsung-870-qvo-2tb', 'SSD накопитель Samsung 870 QVO 2TB SATA III', 14500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-005', id, 'Samsung 870 QVO 4TB', 'samsung-870-qvo-4tb', 'SSD накопитель Samsung 870 QVO 4TB SATA III', 26500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-006', id, 'Kingston A400 480GB', 'kingston-a400-480gb', 'SSD накопитель Kingston A400 480GB SATA III', 3500.00, 30, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-007', id, 'Kingston A400 960GB', 'kingston-a400-960gb', 'SSD накопитель Kingston A400 960GB SATA III', 6200.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-008', id, 'Crucial BX500 500GB', 'crucial-bx500-500gb', 'SSD накопитель Crucial BX500 500GB SATA III', 4000.00, 30, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-009', id, 'Crucial BX500 1TB', 'crucial-bx500-1tb', 'SSD накопитель Crucial BX500 1TB SATA III', 7200.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-010', id, 'WD Blue 500GB', 'wd-blue-500gb', 'SSD накопитель WD Blue 500GB SATA III', 5200.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

-- NVMe SSD
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-001', id, 'Samsung 980 500GB', 'samsung-980-500gb', 'SSD накопитель Samsung 980 500GB NVMe M.2', 5500.00, 25, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-002', id, 'Samsung 980 1TB', 'samsung-980-1tb', 'SSD накопитель Samsung 980 1TB NVMe M.2', 9000.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-003', id, 'Samsung 980 PRO 500GB', 'samsung-980-pro-500gb', 'SSD накопитель Samsung 980 PRO 500GB NVMe M.2 PCIe 4.0', 7200.00, 18, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-004', id, 'Samsung 980 PRO 1TB', 'samsung-980-pro-1tb', 'SSD накопитель Samsung 980 PRO 1TB NVMe M.2 PCIe 4.0', 11500.00, 15, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-005', id, 'Samsung 980 PRO 2TB', 'samsung-980-pro-2tb', 'SSD накопитель Samsung 980 PRO 2TB NVMe M.2 PCIe 4.0', 21500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-006', id, 'Samsung 990 PRO 1TB', 'samsung-990-pro-1tb', 'SSD накопитель Samsung 990 PRO 1TB NVMe M.2 PCIe 4.0', 13500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-007', id, 'Samsung 990 PRO 2TB', 'samsung-990-pro-2tb', 'SSD накопитель Samsung 990 PRO 2TB NVMe M.2 PCIe 4.0', 24500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-008', id, 'WD Black SN770 500GB', 'wd-black-sn770-500gb', 'SSD накопитель WD Black SN770 500GB NVMe M.2 PCIe 4.0', 5200.00, 20, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-009', id, 'WD Black SN770 1TB', 'wd-black-sn770-1tb', 'SSD накопитель WD Black SN770 1TB NVMe M.2 PCIe 4.0', 8500.00, 18, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-010', id, 'WD Black SN770 2TB', 'wd-black-sn770-2tb', 'SSD накопитель WD Black SN770 2TB NVMe M.2 PCIe 4.0', 15500.00, 12, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-011', id, 'Kingston KC3000 1TB', 'kingston-kc3000-1tb', 'SSD накопитель Kingston KC3000 1TB NVMe M.2 PCIe 4.0', 9500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-012', id, 'Kingston KC3000 2TB', 'kingston-kc3000-2tb', 'SSD накопитель Kingston KC3000 2TB NVMe M.2 PCIe 4.0', 17500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-013', id, 'Crucial P3 Plus 500GB', 'crucial-p3-plus-500gb', 'SSD накопитель Crucial P3 Plus 500GB NVMe M.2 PCIe 4.0', 4500.00, 22, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-014', id, 'Crucial P3 Plus 1TB', 'crucial-p3-plus-1tb', 'SSD накопитель Crucial P3 Plus 1TB NVMe M.2 PCIe 4.0', 7500.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-015', id, 'Crucial P3 Plus 2TB', 'crucial-p3-plus-2tb', 'SSD накопитель Crucial P3 Plus 2TB NVMe M.2 PCIe 4.0', 13500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

-- HDD
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-001', id, 'Seagate BarraCuda 1TB', 'seagate-barracuda-1tb', 'Жесткий диск Seagate BarraCuda 1TB 7200rpm', 4200.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-002', id, 'Seagate BarraCuda 2TB', 'seagate-barracuda-2tb', 'Жесткий диск Seagate BarraCuda 2TB 7200rpm', 5600.00, 20, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-003', id, 'Seagate BarraCuda 4TB', 'seagate-barracuda-4tb', 'Жесткий диск Seagate BarraCuda 4TB 5400rpm', 9500.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-004', id, 'WD Blue 1TB', 'wd-blue-1tb', 'Жесткий диск WD Blue 1TB 7200rpm', 4200.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-005', id, 'WD Blue 2TB', 'wd-blue-2tb', 'Жесткий диск WD Blue 2TB 7200rpm', 5800.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-006', id, 'WD Black 1TB', 'wd-black-1tb', 'Жесткий диск WD Black 1TB 7200rpm', 6200.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-007', id, 'Toshiba P300 2TB', 'toshiba-p300-2tb', 'Жесткий диск Toshiba P300 2TB 7200rpm', 5400.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

-- ==================== БЛОКИ ПИТАНИЯ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-001', id, 'Corsair CV550', 'corsair-cv550', 'Блок питания Corsair CV550 550W, 80+ Bronze', 4500.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-002', id, 'Corsair CV650', 'corsair-cv650', 'Блок питания Corsair CV650 650W, 80+ Bronze', 5200.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-003', id, 'Corsair CX650F', 'corsair-cx650f', 'Блок питания Corsair CX650F RGB 650W, 80+ Bronze', 6500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-004', id, 'Corsair RM650', 'corsair-rm650', 'Блок питания Corsair RM650 650W, 80+ Gold', 8200.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-005', id, 'Corsair RM750', 'corsair-rm750', 'Блок питания Corsair RM750 750W, 80+ Gold', 9500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-006', id, 'Corsair RM850', 'corsair-rm850', 'Блок питания Corsair RM850 850W, 80+ Gold', 11200.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-007', id, 'Corsair RM1000e', 'corsair-rm1000e', 'Блок питания Corsair RM1000e 1000W, 80+ Gold', 14500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-008', id, 'Corsair HX1000', 'corsair-hx1000', 'Блок питания Corsair HX1000 1000W, 80+ Platinum', 18500.00, 4, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-009', id, 'Chieftec GPS-500', 'chieftec-gps-500', 'Блок питания Chieftec GPS-500A8 500W', 3500.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-010', id, 'Chieftec GPS-600', 'chieftec-gps-600', 'Блок питания Chieftec GPS-600A8 600W', 4200.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-011', id, 'be quiet! System Power 9 500W', 'bequiet-system-power-500', 'Блок питания be quiet! System Power 9 500W', 4800.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-012', id, 'be quiet! Pure Power 11 600W', 'bequiet-pure-power-600', 'Блок питания be quiet! Pure Power 11 600W, 80+ Gold', 6500.00, 12, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-013', id, 'be quiet! Straight Power 11 850W', 'bequiet-straight-power-850', 'Блок питания be quiet! Straight Power 11 850W, 80+ Platinum', 12500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-014', id, 'DeepCool PF500', 'deepcool-pf500', 'Блок питания DeepCool PF500 500W', 3200.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-015', id, 'DeepCool PQ750M', 'deepcool-pq750m', 'Блок питания DeepCool PQ750M 750W, 80+ Gold', 8500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-016', id, 'DeepCool PX1000G', 'deepcool-px1000g', 'Блок питания DeepCool PX1000G 1000W, 80+ Gold', 14500.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-017', id, 'Seasonic Focus GX-650', 'seasonic-focus-gx-650', 'Блок питания Seasonic Focus GX-650 650W, 80+ Gold', 9500.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-018', id, 'Seasonic Focus GX-750', 'seasonic-focus-gx-750', 'Блок питания Seasonic Focus GX-750 750W, 80+ Gold', 10500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-019', id, 'Seasonic Prime TX-1000', 'seasonic-prime-tx-1000', 'Блок питания Seasonic Prime TX-1000 1000W, 80+ Titanium', 22500.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

-- ==================== КОРПУСА ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-001', id, 'DeepCool CC560', 'deepcool-cc560', 'Корпус DeepCool CC560, Midi-Tower, 4 вентилятора', 4500.00, 20, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-002', id, 'DeepCool CC660', 'deepcool-cc660', 'Корпус DeepCool CC660, Midi-Tower, стекло, 3 вентилятора', 5200.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-003', id, 'DeepCool CH370', 'deepcool-ch370', 'Корпус DeepCool CH370, Midi-Tower, белый, стекло', 5800.00, 12, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-004', id, 'DeepCool CH560', 'deepcool-ch560', 'Корпус DeepCool CH560, Midi-Tower, 4 вентилятора, цифровой дисплей', 8500.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-005', id, 'Corsair 4000D', 'corsair-4000d', 'Корпус Corsair 4000D Airflow, Midi-Tower, белый', 8500.00, 12, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-006', id, 'Corsair 4000D RGB', 'corsair-4000d-rgb', 'Корпус Corsair 4000D RGB Airflow, Midi-Tower, 3 вентилятора', 10500.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-007', id, 'Corsair 5000D', 'corsair-5000d', 'Корпус Corsair 5000D Airflow, Midi-Tower, стекло', 12500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-008', id, 'Corsair iCUE 7000X', 'corsair-icue-7000x', 'Корпус Corsair iCUE 7000X RGB, Full-Tower, стекло, 4 вентилятора', 26500.00, 4, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-009', id, 'Zalman S2', 'zalman-s2', 'Корпус Zalman S2, Midi-Tower, 4 вентилятора', 3800.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-010', id, 'Zalman N5', 'zalman-n5', 'Корпус Zalman N5, Midi-Tower, стекло', 4200.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-011', id, 'Zalman i4', 'zalman-i4', 'Корпус Zalman i4, Midi-Tower, RGB, 4 вентилятора', 5200.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-012', id, 'be quiet! Pure Base 500', 'bequiet-pure-base-500', 'Корпус be quiet! Pure Base 500, Midi-Tower, черный', 6200.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-013', id, 'be quiet! Pure Base 500DX', 'bequiet-pure-base-500dx', 'Корпус be quiet! Pure Base 500DX, Midi-Tower, ARGB', 8200.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-014', id, 'be quiet! Dark Base 700', 'bequiet-dark-base-700', 'Корпус be quiet! Dark Base 700, Full-Tower, стекло', 16500.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-015', id, 'Lian Li Lancool 215', 'lianli-lancool-215', 'Корпус Lian Li Lancool 215, Midi-Tower, 2x200mm вентилятора', 7200.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-016', id, 'Lian Li Lancool 216', 'lianli-lancool-216', 'Корпус Lian Li Lancool 216, Midi-Tower, 2x160mm + 1x140mm', 8500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-017', id, 'Lian Li O11 Dynamic', 'lianli-o11-dynamic', 'Корпус Lian Li O11 Dynamic, Midi-Tower, стекло, 3 вентилятора', 11500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-018', id, 'NZXT H5 Flow', 'nzxt-h5-flow', 'Корпус NZXT H5 Flow, Midi-Tower, белый', 7500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-019', id, 'NZXT H7 Flow', 'nzxt-h7-flow', 'Корпус NZXT H7 Flow, Midi-Tower, черный', 9500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-020', id, 'NZXT H9 Elite', 'nzxt-h9-elite', 'Корпус NZXT H9 Elite, Dual-Chamber, стекло, 3 вентилятора', 17500.00, 5, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';

-- ==================== СИСТЕМЫ ОХЛАЖДЕНИЯ ====================
-- Воздушное охлаждение
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-001', id, 'DeepCool AK400', 'deepcool-ak400', 'Кулер DeepCool AK400, 4 теплотрубки, 120mm вентилятор', 2400.00, 25, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-002', id, 'DeepCool AK500', 'deepcool-ak500', 'Кулер DeepCool AK500, 5 теплотрубок, 120mm вентилятор', 3500.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-003', id, 'DeepCool AK620', 'deepcool-ak620', 'Кулер DeepCool AK620, двухбашенный, 6 теплотрубок, 2x120mm', 4800.00, 18, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-004', id, 'DeepCool AG400', 'deepcool-ag400', 'Кулер DeepCool AG400, 4 теплотрубки, 120mm ARGB', 2300.00, 25, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-005', id, 'DeepCool AG620', 'deepcool-ag620', 'Кулер DeepCool AG620, двухбашенный, 6 теплотрубок, 2x120mm ARGB', 5200.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-006', id, 'Noctua NH-D15', 'noctua-nh-d15', 'Кулер Noctua NH-D15, двухбашенный, 6 теплотрубок, 2x140mm', 9500.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-007', id, 'Noctua NH-U12S', 'noctua-nh-u12s', 'Кулер Noctua NH-U12S, 4 теплотрубки, 120mm', 5500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-008', id, 'be quiet! Dark Rock 4', 'bequiet-dark-rock-4', 'Кулер be quiet! Dark Rock 4, 6 теплотрубок, 135mm', 6200.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-009', id, 'be quiet! Dark Rock Pro 4', 'bequiet-dark-rock-pro-4', 'Кулер be quiet! Dark Rock Pro 4, двухбашенный, 7 теплотрубок, 135+120mm', 8200.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-010', id, 'be quiet! Pure Rock 2', 'bequiet-pure-rock-2', 'Кулер be quiet! Pure Rock 2, 4 теплотрубки, 120mm', 3200.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-011', id, 'ID-Cooling SE-214-XT', 'idcooling-se-214-xt', 'Кулер ID-Cooling SE-214-XT, 4 теплотрубки, 120mm', 1800.00, 30, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-012', id, 'ID-Cooling SE-224-XTS', 'idcooling-se-224-xts', 'Кулер ID-Cooling SE-224-XTS, 4 теплотрубки, 120mm ARGB', 2100.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-013', id, 'ID-Cooling SE-226-XT', 'idcooling-se-226-xt', 'Кулер ID-Cooling SE-226-XT, 6 теплотрубок, 120mm', 2800.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-014', id, 'Thermalright Peerless Assassin 120', 'thermalright-peerless-assassin-120', 'Кулер Thermalright Peerless Assassin 120, двухбашенный, 7 теплотрубок, 2x120mm', 3500.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

-- Жидкостное охлаждение
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-001', id, 'DeepCool LS520', 'deepcool-ls520', 'СЖО DeepCool LS520, 240мм, ARGB', 6800.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-002', id, 'DeepCool LS720', 'deepcool-ls720', 'СЖО DeepCool LS720, 360мм, ARGB', 8500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-003', id, 'DeepCool LT520', 'deepcool-lt520', 'СЖО DeepCool LT520, 240мм, ARGB', 7200.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-004', id, 'DeepCool LT720', 'deepcool-lt720', 'СЖО DeepCool LT720, 360мм, ARGB', 9500.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-005', id, 'Corsair iCUE H100i', 'corsair-icue-h100i', 'СЖО Corsair iCUE H100i RGB, 240мм', 9500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-006', id, 'Corsair iCUE H150i', 'corsair-icue-h150i', 'СЖО Corsair iCUE H150i RGB, 360мм', 12500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-007', id, 'Corsair iCUE H170i', 'corsair-icue-h170i', 'СЖО Corsair iCUE H170i RGB, 420мм', 16500.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-008', id, 'MSI MAG CoreLiquid 240R', 'msi-mag-coreliquid-240r', 'СЖО MSI MAG CoreLiquid 240R, 240мм', 6500.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-009', id, 'MSI MPG CoreLiquid K360', 'msi-mpg-coreliquid-k360', 'СЖО MSI MPG CoreLiquid K360, 360мм, дисплей', 16500.00, 6, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-010', id, 'NZXT Kraken 240', 'nzxt-kraken-240', 'СЖО NZXT Kraken 240 RGB, 240мм', 9500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-011', id, 'NZXT Kraken 360', 'nzxt-kraken-360', 'СЖО NZXT Kraken 360 RGB, 360мм', 12500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-012', id, 'be quiet! Silent Loop 2 240', 'bequiet-silent-loop-2-240', 'СЖО be quiet! Silent Loop 2 240мм', 8500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-013', id, 'be quiet! Silent Loop 2 360', 'bequiet-silent-loop-2-360', 'СЖО be quiet! Silent Loop 2 360мм', 11500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

--changeset tyzhprogramist:22
-- ==================== СВЯЗИ ТОВАРОВ ====================
-- Связи для новых товаров
INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-003' AND p2.sku = 'GPU-NVDA-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-003' AND p2.sku = 'RAM-DDR4-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-003' AND p2.sku = 'SSD-NVME-002';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-AMD-004' AND p2.sku = 'GPU-AMD-003';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-AMD-004' AND p2.sku = 'MB-001';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-AMD-010' AND p2.sku = 'GPU-NVDA-015';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-011' AND p2.sku = 'GPU-NVDA-019';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'GPU-NVDA-002' AND p2.sku = 'PSU-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'GPU-NVDA-019' AND p2.sku = 'PSU-008';

-- Связи для старых товаров (CPU-001, GPU-001 и т.д.)
INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'GPU-001';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-002' AND p2.sku = 'GPU-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'RAM-001';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'SSD-001';


--changeset tyzhprogramist:23
-- ==================== ПЕРИФЕРИЯ И АКСЕССУАРЫ ====================
-- Insert new peripheral categories
INSERT INTO categories (name, slug, sort_order) VALUES
('Клавиатуры', 'keyboards', 9),
('Мыши', 'mice', 10),
('Коврики для мыши', 'mouse-pads', 11),
('Веб-камеры', 'webcams', 12),
('Наушники и гарнитуры', 'headphones', 13),
('Колонки', 'speakers', 14),
('Сетевое оборудование', 'networking', 15);

-- ==================== КЛАВИАТУРЫ ====================
-- Механические клавиатуры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-001', id, 'Logitech G Pro X Mechanical', 'logitech-g-pro-x-mech', 'Механическая клавиатура Logitech G Pro X, Hot-Swap переключатели, RGB', 14500.00, 8, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-002', id, 'Razer BlackWidow V4', 'razer-blackwidow-v4', 'Механическая клавиатура Razer BlackWidow V4, Green Switches, RGB', 16500.00, 6, NOW(), TRUE, TRUE, FALSE, 24
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-003', id, 'Corsair K70 RGB PRO', 'corsair-k70-rgb-pro', 'Механическая клавиатура Corsair K70 RGB PRO, Cherry MX Red, RGB', 15500.00, 7, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-004', id, 'Keychron K8 Pro', 'keychron-k8-pro', 'Механическая клавиатура Keychron K8 Pro, Gateron G Pro, QMK/VIA, Bluetooth', 9500.00, 5, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-005', id, 'Ducky One 3 Mini', 'ducky-one-3-mini', 'Механическая клавиатура Ducky One 3 Mini 60%, Cherry MX Speed Silver', 11500.00, 4, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'keyboards';

-- Мембранные клавиатуры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-001', id, 'Logitech MK270', 'logitech-mk270', 'Комплект клавиатуры и мыши Logitech MK270, беспроводной', 3500.00, 20, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-002', id, 'Logitech K380', 'logitech-k380', 'Компактная Bluetooth клавиатура Logitech K380, мультидевайс', 4200.00, 12, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-003', id, 'Logitech MX Keys', 'logitech-mx-keys', 'Беспроводная клавиатура Logitech MX Keys, подсветка, для работы', 9500.00, 8, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-004', id, 'Microsoft Ergonomic', 'microsoft-ergonomic', 'Эргономичная клавиатура Microsoft Ergonomic, USB', 6800.00, 6, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-005', id, 'Apple Magic Keyboard', 'apple-magic-keyboard', 'Apple Magic Keyboard, беспроводная, для Mac', 10500.00, 7, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

-- ==================== МЫШИ ====================
-- Игровые мыши
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-001', id, 'Logitech G PRO X Superlight', 'logitech-g-pro-x-superlight', 'Игровая мышь Logitech G PRO X Superlight, 25000 DPI, 63g, беспроводная', 13500.00, 10, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-002', id, 'Razer DeathAdder V3 Pro', 'razer-deathadder-v3-pro', 'Игровая мышь Razer DeathAdder V3 Pro, 30000 DPI, беспроводная', 12500.00, 8, NOW(), TRUE, TRUE, FALSE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-003', id, 'Razer Viper V2 Pro', 'razer-viper-v2-pro', 'Игровая мышь Razer Viper V2 Pro, 30000 DPI, 58g, беспроводная', 12500.00, 7, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-004', id, 'Logitech G502 X PLUS', 'logitech-g502-x-plus', 'Игровая мышь Logitech G502 X PLUS, LIGHTFORCE, беспроводная, RGB', 11500.00, 9, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-005', id, 'SteelSeries Aerox 5', 'steelseries-aerox-5', 'Игровая мышь SteelSeries Aerox 5 Wireless, 18000 DPI, ультралегкая', 9500.00, 6, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-006', id, 'Zowie EC2-C', 'zowie-ec2-c', 'Игровая мышь Zowie EC2-C, 3200 DPI, проводная, для киберспорта', 6800.00, 5, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-007', id, 'Corsair Sabre RGB Pro', 'corsair-sabre-rgb-pro', 'Игровая мышь Corsair Sabre RGB Pro, проводная, 18000 DPI', 5500.00, 8, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mice';

-- Офисные мыши
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-001', id, 'Logitech MX Master 3S', 'logitech-mx-master-3s', 'Беспроводная мышь Logitech MX Master 3S, 8000 DPI, для работы', 9500.00, 12, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-002', id, 'Logitech MX Anywhere 3S', 'logitech-mx-anywhere-3s', 'Компактная мышь Logitech MX Anywhere 3S, беспроводная', 7500.00, 10, NOW(), TRUE, TRUE, FALSE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-003', id, 'Logitech M185', 'logitech-m185', 'Беспроводная мышь Logitech M185, компактная', 1200.00, 30, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-004', id, 'Microsoft Bluetooth Mouse', 'microsoft-bluetooth-mouse', 'Microsoft Bluetooth Mouse, эргономичная', 2800.00, 15, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'mice';

-- ==================== КОВРИКИ ДЛЯ МЫШИ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-001', id, 'Razer Strider XXL', 'razer-strider-xxl', 'Игровой коврик Razer Strider, размер XXL, 940x410x3мм', 3500.00, 15, NOW(), TRUE, TRUE, TRUE, 12
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-002', id, 'Logitech G840', 'logitech-g840', 'Игровой коврик Logitech G840, размер XL, 900x400x3мм', 4500.00, 12, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-003', id, 'SteelSeries QcK Heavy', 'steelseries-qck-heavy', 'Игровой коврик SteelSeries QcK Heavy, размер L, 450x400x6мм', 2800.00, 20, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-004', id, 'SteelSeries QcK Prism', 'steelseries-qck-prism', 'Игровой коврик SteelSeries QcK Prism с подсветкой, XL', 5800.00, 8, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-005', id, 'Corsair MM350', 'corsair-mm350', 'Игровой коврик Corsair MM350 Champion Series, XL', 3200.00, 10, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mouse-pads';

-- ==================== ВЕБ-КАМЕРЫ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'WEBCAM-001', id, 'Logitech StreamCam', 'logitech-streamcam', 'Веб-камера Logitech StreamCam, 1080p 60fps, USB-C', 11500.00, 8, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'webcams';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'WEBCAM-002', id, 'Logitech C920', 'logitech-c920', 'Веб-камера Logitech C920 HD Pro, 1080p', 6500.00, 15, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'webcams';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'WEBCAM-003', id, 'Logitech BRIO 4K', 'logitech-brio-4k', 'Веб-камера Logitech BRIO 4K, 4K Ultra HD, HDR', 18500.00, 5, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'webcams';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'WEBCAM-004', id, 'Razer Kiyo Pro', 'razer-kiyo-pro', 'Веб-камера Razer Kiyo Pro, 1080p 60fps, HDR', 12500.00, 6, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'webcams';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'WEBCAM-005', id, 'AverMedia Live Streamer 313', 'avermedia-live-streamer-313', 'Веб-камера AverMedia Live Streamer 313, 1080p 60fps', 7500.00, 7, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'webcams';

-- ==================== НАУШНИКИ И ГАРНИТУРЫ ====================
-- Проводные
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WIRED-001', id, 'HyperX Cloud II', 'hyperx-cloud-ii', 'Игровая гарнитура HyperX Cloud II, 7.1 виртуальный звук', 6500.00, 15, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'headphones';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WIRED-002', id, 'HyperX Cloud Alpha', 'hyperx-cloud-alpha', 'Игровая гарнитура HyperX Cloud Alpha, двойные камеры', 7500.00, 12, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'headphones';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WIRED-003', id, 'SteelSeries Arctis 5', 'steelseries-arctis-5', 'Игровая гарнитура SteelSeries Arctis 5, RGB, DTS', 8500.00, 10, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'headphones';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WIRED-004', id, 'Logitech G PRO X', 'logitech-g-pro-x', 'Игровая гарнитура Logitech G PRO X, с микрофоном', 9500.00, 8, NOW(), TRUE, TRUE, FALSE, 24
FROM categories WHERE slug = 'headphones';

-- Беспроводные
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WLESS-001', id, 'Logitech G PRO X Wireless', 'logitech-g-pro-x-wireless', 'Беспроводная гарнитура Logitech G PRO X, Lightspeed', 14500.00, 6, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'headphones';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WLESS-002', id, 'SteelSeries Arctis 7+', 'steelseries-arctis-7-plus', 'Беспроводная гарнитура SteelSeries Arctis 7+, 30ч работы', 11500.00, 8, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'headphones';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WLESS-003', id, 'Sony WH-1000XM4', 'sony-wh-1000xm4', 'Беспроводные наушники Sony WH-1000XM4, шумоподавление', 22500.00, 7, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'headphones';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WLESS-004', id, 'Sony WH-1000XM5', 'sony-wh-1000xm5', 'Беспроводные наушники Sony WH-1000XM5, шумоподавление', 28500.00, 5, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'headphones';

-- ==================== КОЛОНКИ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-001', id, 'Logitech Z333', 'logitech-z333', 'Акустическая система Logitech Z333, 2.1, 40Вт', 5500.00, 12, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'speakers';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-002', id, 'Logitech Z623', 'logitech-z623', 'Акустическая система Logitech Z623, 2.1, 200Вт, THX', 13500.00, 8, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'speakers';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-003', id, 'Edifier R1280DB', 'edifier-r1280db', 'Активные колонки Edifier R1280DB, 2.0, 42Вт, Bluetooth', 11500.00, 10, NOW(), TRUE, TRUE, TRUE, 12
FROM categories WHERE slug = 'speakers';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-004', id, 'Edifier S350DB', 'edifier-s350db', 'Акустическая система Edifier S350DB, 2.1, 150Вт', 18500.00, 5, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'speakers';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-005', id, 'JBL Charge 5', 'jbl-charge-5', 'Портативная колонка JBL Charge 5, Bluetooth', 11500.00, 10, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'speakers';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-006', id, 'JBL Flip 6', 'jbl-flip-6', 'Портативная колонка JBL Flip 6, Bluetooth', 8500.00, 12, NOW(), TRUE, TRUE, TRUE, 12
FROM categories WHERE slug = 'speakers';

-- ==================== СЕТЕВОЕ ОБОРУДОВАНИЕ ====================
-- Роутеры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-001', id, 'TP-Link Archer AX73', 'tp-link-archer-ax73', 'Wi-Fi роутер TP-Link Archer AX73, AX5400, гигабитный', 7500.00, 12, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-002', id, 'TP-Link Archer AX11000', 'tp-link-archer-ax11000', 'Wi-Fi роутер TP-Link Archer AX11000, три-диапазонный', 23500.00, 4, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-003', id, 'ASUS RT-AX82U', 'asus-rt-ax82u', 'Wi-Fi роутер ASUS RT-AX82U, AX5400, RGB', 12500.00, 8, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-004', id, 'ASUS ROG Rapture GT-AX11000', 'asus-rog-rapture-gt-ax11000', 'Игровой роутер ASUS ROG Rapture GT-AX11000', 32500.00, 3, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-005', id, 'MikroTik hAP ac2', 'mikrotik-hap-ac2', 'Роутер MikroTik hAP ac2, гигабитный', 5500.00, 10, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';

-- Сетевые карты и адаптеры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-ADP-001', id, 'TP-Link Archer TX50E', 'tp-link-archer-tx50e', 'PCIe адаптер TP-Link Archer TX50E, Wi-Fi 6, Bluetooth 5.0', 4200.00, 10, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-ADP-002', id, 'ASUS PCE-AX58BT', 'asus-pce-ax58bt', 'PCIe адаптер ASUS PCE-AX58BT, Wi-Fi 6, Bluetooth 5.0', 4800.00, 8, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-ADP-003', id, 'TP-Link Archer T3U', 'tp-link-archer-t3u', 'USB адаптер TP-Link Archer T3U, Wi-Fi AC1300', 1500.00, 20, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';

-- Коммутаторы
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-SW-001', id, 'TP-Link TL-SG105', 'tp-link-tl-sg105', 'Коммутатор TP-Link TL-SG105, 5 портов гигабитный', 1500.00, 15, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-SW-002', id, 'TP-Link TL-SG108', 'tp-link-tl-sg108', 'Коммутатор TP-Link TL-SG108, 8 портов гигабитный', 2200.00, 12, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-SW-003', id, 'MikroTik CRS112-8P-4S-IN', 'mikrotik-crs112-8p-4s-in', 'Коммутатор MikroTik CRS112-8P-4S-IN, 8 портов PoE', 11500.00, 5, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';

--changeset tyzhprogramist:24
-- ==================== ГОТОВЫЕ ПК И НОУТБУКИ ====================
-- Insert new categories for PCs and Laptops
INSERT INTO categories (name, slug, sort_order) VALUES
('Готовые ПК', 'prebuilt-pcs', 16),
('Игровые ПК', 'gaming-pcs', 17),
('Офисные ПК', 'office-pcs', 18),
('Ноутбуки', 'laptops', 19),
('Игровые ноутбуки', 'gaming-laptops', 20),
('Ноутбуки для работы', 'work-laptops', 21);

-- ==================== ГОТОВЫЕ ПК ====================
-- Офисные ПК
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-OFFICE-001', id, 'Компьютер Office Basic', 'pc-office-basic', 'ПК для офиса и дома: Intel Celeron G6900, 8GB DDR4, SSD 256GB, без ОС', 24500.00, 26500.00, 10, NOW(), TRUE, FALSE, FALSE, 12, 4.2
FROM categories WHERE slug = 'office-pcs';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-OFFICE-002', id, 'Компьютер Office Standard', 'pc-office-standard', 'ПК для работы: Intel Core i3-12100, 16GB DDR4, SSD 480GB, Windows 11 Pro', 38500.00, 41500.00, 8, NOW(), TRUE, TRUE, TRUE, 24, 4.5
FROM categories WHERE slug = 'office-pcs';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-OFFICE-003', id, 'Компьютер Office Premium', 'pc-office-premium', 'ПК для бизнеса: Intel Core i5-12400, 16GB DDR4, SSD 512GB + HDD 1TB, Windows 11 Pro', 52500.00, 56500.00, 5, NOW(), TRUE, FALSE, FALSE, 24, 4.7
FROM categories WHERE slug = 'office-pcs';

-- Игровые ПК (Начальный уровень)
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-001', id, 'Игровой ПК Starter', 'gaming-pc-starter', 'Начальный игровой ПК: Intel Core i3-12100F, GTX 1650 4GB, 16GB DDR4, SSD 480GB', 52500.00, 56500.00, 7, NOW(), TRUE, FALSE, TRUE, 24, 4.3
FROM categories WHERE slug = 'gaming-pcs';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-002', id, 'Игровой ПК Essential', 'gaming-pc-essential', 'Игровой ПК: Intel Core i5-12400F, RTX 3050 8GB, 16GB DDR4, SSD 512GB', 68500.00, 72500.00, 6, NOW(), TRUE, TRUE, FALSE, 24, 4.6
FROM categories WHERE slug = 'gaming-pcs';

-- Игровые ПК (Средний уровень)
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-003', id, 'Игровой ПК Mid Gaming', 'gaming-pc-mid', 'Средний игровой ПК: Intel Core i5-13400F, RTX 4060 8GB, 16GB DDR5, SSD 1TB', 89500.00, 94500.00, 5, NOW(), TRUE, FALSE, TRUE, 24, 4.8
FROM categories WHERE slug = 'gaming-pcs';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-004', id, 'Игровой ПК Pro', 'gaming-pc-pro', 'Профессиональный игровой ПК: Intel Core i5-13600K, RTX 4070 12GB, 32GB DDR5, SSD 1TB NVMe', 129500.00, 137500.00, 4, NOW(), TRUE, TRUE, TRUE, 36, 4.9
FROM categories WHERE slug = 'gaming-pcs';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-005', id, 'Игровой ПК Ultra', 'gaming-pc-ultra', 'Мощный игровой ПК: Intel Core i7-13700K, RTX 4070 Ti 12GB, 32GB DDR5, SSD 2TB NVMe', 159500.00, 169500.00, 3, NOW(), TRUE, FALSE, FALSE, 36, 4.7
FROM categories WHERE slug = 'gaming-pcs';

-- Игровые ПК (Высокий уровень)
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-006', id, 'Игровой ПК Elite', 'gaming-pc-elite', 'Элитный игровой ПК: Intel Core i7-14700K, RTX 4080 16GB, 64GB DDR5, SSD 2TB NVMe', 219500.00, 235000.00, 2, NOW(), TRUE, TRUE, TRUE, 36, 5.0
FROM categories WHERE slug = 'gaming-pcs';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-007', id, 'Игровой ПК Ultimate', 'gaming-pc-ultimate', 'Топовый игровой ПК: Intel Core i9-14900K, RTX 4090 24GB, 64GB DDR5, SSD 4TB NVMe', 329500.00, 349500.00, 1, NOW(), TRUE, TRUE, TRUE, 36, 5.0
FROM categories WHERE slug = 'gaming-pcs';

-- ПК на AMD
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-008', id, 'Игровой ПК AMD Ryzen', 'gaming-pc-amd-ryzen', 'Игровой ПК на AMD: Ryzen 5 7500F, RX 7700 XT 12GB, 32GB DDR5, SSD 1TB NVMe', 109500.00, 115500.00, 4, NOW(), TRUE, FALSE, FALSE, 24, 4.6
FROM categories WHERE slug = 'gaming-pcs';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-009', id, 'Игровой ПК AMD X3D', 'gaming-pc-amd-x3d', 'Игровой ПК: Ryzen 7 7800X3D, RX 7800 XT 16GB, 32GB DDR5, SSD 2TB NVMe', 149500.00, 159500.00, 3, NOW(), TRUE, TRUE, TRUE, 36, 4.8
FROM categories WHERE slug = 'gaming-pcs';

-- ==================== НОУТБУКИ ====================
-- Ноутбуки для работы и учебы
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-WORK-001', id, 'Ноутбук Acer Aspire 3', 'acer-aspire-3', 'Ноутбук Acer Aspire 3: Intel Celeron N4500, 4GB, 128GB SSD, 15.6" HD', 26500.00, 28500.00, 12, NOW(), TRUE, FALSE, FALSE, 12, 4.0
FROM categories WHERE slug = 'work-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-WORK-002', id, 'Ноутбук HP 255 G9', 'hp-255-g9', 'Ноутбук HP 255 G9: AMD Ryzen 3 3250U, 8GB, 256GB SSD, 15.6" FHD', 38500.00, 41500.00, 10, NOW(), TRUE, FALSE, TRUE, 12, 4.3
FROM categories WHERE slug = 'work-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-WORK-003', id, 'Ноутбук Lenovo IdeaPad 3', 'lenovo-ideapad-3', 'Ноутбук Lenovo IdeaPad 3: Intel Core i3-1215U, 8GB, 512GB SSD, 15.6" FHD', 44500.00, 47500.00, 8, NOW(), TRUE, TRUE, FALSE, 12, 4.5
FROM categories WHERE slug = 'work-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-WORK-004', id, 'Ноутбук ASUS Vivobook 15', 'asus-vivobook-15', 'Ноутбук ASUS Vivobook 15: Intel Core i5-1235U, 16GB, 512GB SSD, 15.6" FHD', 58500.00, 62500.00, 7, NOW(), TRUE, FALSE, TRUE, 24, 4.6
FROM categories WHERE slug = 'work-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-WORK-005', id, 'Ноутбук Dell Inspiron 15', 'dell-inspiron-15', 'Ноутбук Dell Inspiron 15: Intel Core i7-1255U, 16GB, 1TB SSD, 15.6" FHD', 78500.00, 83500.00, 5, NOW(), TRUE, FALSE, FALSE, 24, 4.7
FROM categories WHERE slug = 'work-laptops';

-- Ультрабуки и премиум
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-ULTRA-001', id, 'Ноутбук Apple MacBook Air 13" M1', 'macbook-air-m1-13', 'Ноутбук Apple MacBook Air 13": M1, 8GB, 256GB SSD, Retina', 89900.00, 94900.00, 8, NOW(), TRUE, FALSE, TRUE, 12, 4.9
FROM categories WHERE slug = 'laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-ULTRA-002', id, 'Ноутбук Apple MacBook Air 15" M2', 'macbook-air-m2-15', 'Ноутбук Apple MacBook Air 15": M2, 8GB, 512GB SSD, Retina', 129900.00, 135900.00, 5, NOW(), TRUE, TRUE, TRUE, 12, 5.0
FROM categories WHERE slug = 'laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-ULTRA-003', id, 'Ноутбук Apple MacBook Pro 14" M3', 'macbook-pro-m3-14', 'Ноутбук Apple MacBook Pro 14": M3 Pro, 18GB, 512GB SSD', 179900.00, 189900.00, 4, NOW(), TRUE, TRUE, FALSE, 12, 5.0
FROM categories WHERE slug = 'laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-ULTRA-004', id, 'Ноутбук ASUS ZenBook 14', 'asus-zenbook-14', 'Ноутбук ASUS ZenBook 14: Intel Core i7-1360P, 16GB, 1TB SSD, OLED', 89500.00, 94500.00, 6, NOW(), TRUE, FALSE, FALSE, 24, 4.8
FROM categories WHERE slug = 'laptops';

-- ==================== ИГРОВЫЕ НОУТБУКИ ====================
-- Начальный уровень
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-001', id, 'Ноутбук игровой Acer Nitro 5', 'acer-nitro-5', 'Игровой ноутбук Acer Nitro 5: Intel Core i5-12450H, RTX 3050 4GB, 8GB, 512GB SSD, 15.6" 144Hz', 69900.00, 74900.00, 7, NOW(), TRUE, FALSE, TRUE, 12, 4.4
FROM categories WHERE slug = 'gaming-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-002', id, 'Ноутбук игровой ASUS TUF F15', 'asus-tuf-f15', 'Игровой ноутбук ASUS TUF F15: Intel Core i5-12500H, RTX 3050 Ti 4GB, 16GB, 512GB SSD, 15.6" 144Hz', 74900.00, 79900.00, 6, NOW(), TRUE, FALSE, FALSE, 24, 4.5
FROM categories WHERE slug = 'gaming-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-003', id, 'Ноутбук игровой Lenovo LOQ', 'lenovo-loq', 'Игровой ноутбук Lenovo LOQ: Intel Core i5-13420H, RTX 3050 6GB, 16GB, 512GB SSD, 15.6" 144Hz', 74900.00, 79500.00, 6, NOW(), TRUE, TRUE, TRUE, 24, 4.6
FROM categories WHERE slug = 'gaming-laptops';

-- Средний уровень
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-004', id, 'Ноутбук игровой ASUS ROG Strix G16', 'asus-rog-strix-g16', 'Игровой ноутбук ASUS ROG Strix G16: Intel Core i7-13650HX, RTX 4060 8GB, 16GB, 1TB SSD, 16" 165Hz', 109900.00, 115900.00, 5, NOW(), TRUE, FALSE, TRUE, 24, 4.7
FROM categories WHERE slug = 'gaming-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-005', id, 'Ноутбук игровой MSI Katana 15', 'msi-katana-15', 'Игровой ноутбук MSI Katana 15: Intel Core i7-13620H, RTX 4070 8GB, 16GB, 1TB SSD, 15.6" 144Hz', 114900.00, 121900.00, 4, NOW(), TRUE, TRUE, FALSE, 24, 4.6
FROM categories WHERE slug = 'gaming-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-006', id, 'Ноутбук игровой Lenovo Legion 5', 'lenovo-legion-5', 'Игровой ноутбук Lenovo Legion 5: AMD Ryzen 7 7745HX, RTX 4060 8GB, 16GB, 1TB SSD, 16" 165Hz', 114900.00, 121900.00, 5, NOW(), TRUE, FALSE, TRUE, 24, 4.8
FROM categories WHERE slug = 'gaming-laptops';

-- Высокий уровень
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-007', id, 'Ноутбук игровой ASUS ROG Strix SCAR 16', 'asus-rog-scar-16', 'Игровой ноутбук ASUS ROG Strix SCAR 16: Intel Core i9-14900HX, RTX 4080 12GB, 32GB, 2TB SSD, MiniLED 240Hz', 239900.00, 249900.00, 3, NOW(), TRUE, TRUE, TRUE, 36, 5.0
FROM categories WHERE slug = 'gaming-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-008', id, 'Ноутбук игровой MSI Titan 18 HX', 'msi-titan-18', 'Игровой ноутбук MSI Titan 18 HX: Intel Core i9-14900HX, RTX 4090 16GB, 64GB, 4TB SSD, 18" 4K MiniLED', 399900.00, 419900.00, 1, NOW(), TRUE, TRUE, TRUE, 36, 5.0
FROM categories WHERE slug = 'gaming-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-009', id, 'Ноутбук игровой Razer Blade 16', 'razer-blade-16', 'Игровой ноутбук Razer Blade 16: Intel Core i9-14900HX, RTX 4090 16GB, 32GB, 2TB SSD, 16" Dual Mode MiniLED', 319900.00, 335900.00, 2, NOW(), TRUE, TRUE, FALSE, 36, 4.9
FROM categories WHERE slug = 'gaming-laptops';

-- Ноутбуки на AMD
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-010', id, 'Ноутбук игровой ASUS TUF A16', 'asus-tuf-a16', 'Игровой ноутбук ASUS TUF A16: AMD Ryzen 7 7735HS, RX 7600S 8GB, 16GB, 1TB SSD, 16" 165Hz', 84900.00, 89500.00, 5, NOW(), TRUE, FALSE, FALSE, 24, 4.5
FROM categories WHERE slug = 'gaming-laptops';

INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-011', id, 'Ноутбук игровой Lenovo Legion Pro 7', 'lenovo-legion-pro-7', 'Игровой ноутбук Lenovo Legion Pro 7: AMD Ryzen 9 7945HX, RTX 4080 12GB, 32GB, 2TB SSD, 16" 240Hz', 199900.00, 209900.00, 3, NOW(), TRUE, TRUE, TRUE, 36, 4.8
FROM categories WHERE slug = 'gaming-laptops';