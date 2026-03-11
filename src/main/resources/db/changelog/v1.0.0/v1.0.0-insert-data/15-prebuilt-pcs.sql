--liquibase formatted sql

--changeset tyzhprogramist:24-1
-- Insert new categories for PCs
INSERT INTO categories (name, slug, sort_order) VALUES
('Готовые ПК', 'prebuilt-pcs', 16),
('Игровые ПК', 'gaming-pcs', 17),
('Офисные ПК', 'office-pcs', 18);

-- ==================== ГОТОВЫЕ ПК ====================
-- Офисные ПК
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-OFFICE-001', id, 'Компьютер Office Basic', 'pc-office-basic', 'ПК для офиса и дома: Intel Celeron G6900, 8GB DDR4, SSD 256GB, без ОС', 24500.00, 26500.00, 10, NOW(), TRUE, FALSE, FALSE, 12, 4.2
FROM categories WHERE slug = 'office-pcs';

-- ... (все офисные ПК) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-OFFICE-003', id, 'Компьютер Office Premium', 'pc-office-premium', 'ПК для бизнеса: Intel Core i5-12400, 16GB DDR4, SSD 512GB + HDD 1TB, Windows 11 Pro', 52500.00, 56500.00, 5, NOW(), TRUE, FALSE, FALSE, 24, 4.7
FROM categories WHERE slug = 'office-pcs';

-- Игровые ПК
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-001', id, 'Игровой ПК Starter', 'gaming-pc-starter', 'Начальный игровой ПК: Intel Core i3-12100F, GTX 1650 4GB, 16GB DDR4, SSD 480GB', 52500.00, 56500.00, 7, NOW(), TRUE, FALSE, TRUE, 24, 4.3
FROM categories WHERE slug = 'gaming-pcs';

-- ... (все игровые ПК) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, old_price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months, rating)
SELECT 'PC-GAME-009', id, 'Игровой ПК AMD X3D', 'gaming-pc-amd-x3d', 'Игровой ПК: Ryzen 7 7800X3D, RX 7800 XT 16GB, 32GB DDR5, SSD 2TB NVMe', 149500.00, 159500.00, 3, NOW(), TRUE, TRUE, TRUE, 36, 4.8
FROM categories WHERE slug = 'gaming-pcs';