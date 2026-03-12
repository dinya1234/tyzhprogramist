--liquibase formatted sql

--changeset tyzhprogramist:24-2
-- Insert new categories for Laptops
INSERT INTO categories (name, slug, sort_order) VALUES
('Ноутбуки', 'laptops', 19),
('Ноутбуки для работы', 'work-laptops', 21);

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