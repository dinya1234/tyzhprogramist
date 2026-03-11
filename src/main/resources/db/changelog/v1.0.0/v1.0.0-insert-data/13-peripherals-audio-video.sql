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

-- ... (все веб-камеры) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'WEBCAM-005', id, 'AverMedia Live Streamer 313', 'avermedia-live-streamer-313', 'Веб-камера AverMedia Live Streamer 313, 1080p 60fps', 7500.00, 7, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'webcams';

-- ==================== НАУШНИКИ И ГАРНИТУРЫ ====================
-- Проводные
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WIRED-001', id, 'HyperX Cloud II', 'hyperx-cloud-ii', 'Игровая гарнитура HyperX Cloud II, 7.1 виртуальный звук', 6500.00, 15, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'headphones';

-- ... (все наушники) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'HP-WLESS-004', id, 'Sony WH-1000XM5', 'sony-wh-1000xm5', 'Беспроводные наушники Sony WH-1000XM5, шумоподавление', 28500.00, 5, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'headphones';

-- ==================== КОЛОНКИ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-001', id, 'Logitech Z333', 'logitech-z333', 'Акустическая система Logitech Z333, 2.1, 40Вт', 5500.00, 12, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'speakers';

-- ... (все колонки) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'SPK-006', id, 'JBL Flip 6', 'jbl-flip-6', 'Портативная колонка JBL Flip 6, Bluetooth', 8500.00, 12, NOW(), TRUE, TRUE, TRUE, 12
FROM categories WHERE slug = 'speakers';