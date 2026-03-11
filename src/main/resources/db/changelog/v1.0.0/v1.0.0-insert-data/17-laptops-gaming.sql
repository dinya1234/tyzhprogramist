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