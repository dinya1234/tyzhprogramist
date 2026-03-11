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