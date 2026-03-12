--liquibase formatted sql

--changeset tyzhprogramist:23-2
-- Insert new peripheral categories
INSERT INTO categories (name, slug, sort_order) VALUES
('Веб-камеры', 'webcams', 12),
('Наушники и гарнитуры', 'headphones', 13),
('Колонки', 'speakers', 14);

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