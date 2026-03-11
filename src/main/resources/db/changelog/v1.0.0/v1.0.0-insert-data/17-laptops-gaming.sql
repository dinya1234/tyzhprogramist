--liquibase formatted sql

--changeset tyzhprogramist:24-3
-- Insert new category for Gaming Laptops
INSERT INTO categories (name, slug, sort_order) VALUES
('Игровые ноутбуки', 'gaming-laptops', 20);

-- ==================== ИГРОВЫЕ НОУТБУКИ ====================
-- Начальный уровень
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-001', id, 'Ноутбук игровой Acer Nitro 5', 'acer-nitro-5', 'Игровой ноутбук Acer Nitro 5: Intel Core i5-12450H, RTX 3050 4GB, 8GB, 512GB SSD, 15.6" 144Hz', 69900.00, 74900.00, 7, NOW(), TRUE, FALSE, TRUE, 12, 4.4
FROM categories WHERE slug = 'gaming-laptops';

-- ... (все игровые ноутбуки) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-GAME-011', id, 'Ноутбук игровой Lenovo Legion Pro 7', 'lenovo-legion-pro-7', 'Игровой ноутбук Lenovo Legion Pro 7: AMD Ryzen 9 7945HX, RTX 4080 12GB, 32GB, 2TB SSD, 16" 240Hz', 199900.00, 209900.00, 3, NOW(), TRUE, TRUE, TRUE, 36, 4.8
FROM categories WHERE slug = 'gaming-laptops';