--liquibase formatted sql

--changeset tyzhprogramist:24-2
-- Insert new categories for Laptops
INSERT INTO categories (name, slug, sort_order) VALUES
('Ноутбуки', 'laptops', 19),
('Ноутбуки для работы', 'work-laptops', 21);

-- ==================== НОУТБУКИ (РАБОЧИЕ) ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-WORK-001', id, 'Ноутбук Acer Aspire 3', 'acer-aspire-3', 'Ноутбук Acer Aspire 3: Intel Celeron N4500, 4GB, 128GB SSD, 15.6" HD', 26500.00, 28500.00, 12, NOW(), TRUE, FALSE, FALSE, 12, 4.0
FROM categories WHERE slug = 'work-laptops';

-- ... (все рабочие ноутбуки) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-WORK-005', id, 'Ноутбук Dell Inspiron 15', 'dell-inspiron-15', 'Ноутбук Dell Inspiron 15: Intel Core i7-1255U, 16GB, 1TB SSD, 15.6" FHD', 78500.00, 83500.00, 5, NOW(), TRUE, FALSE, FALSE, 24, 4.7
FROM categories WHERE slug = 'work-laptops';

-- Ультрабуки (в категории 'laptops')
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-ULTRA-001', id, 'Ноутбук Apple MacBook Air 13" M1', 'macbook-air-m1-13', 'Ноутбук Apple MacBook Air 13": M1, 8GB, 256GB SSD, Retina', 89900.00, 94900.00, 8, NOW(), TRUE, FALSE, TRUE, 12, 4.9
FROM categories WHERE slug = 'laptops';

-- ... (все ультрабуки) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'LAP-ULTRA-004', id, 'Ноутбук ASUS ZenBook 14', 'asus-zenbook-14', 'Ноутбук ASUS ZenBook 14: Intel Core i7-1360P, 16GB, 1TB SSD, OLED', 89500.00, 94500.00, 6, NOW(), TRUE, FALSE, FALSE, 24, 4.8
FROM categories WHERE slug = 'laptops';