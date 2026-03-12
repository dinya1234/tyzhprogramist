--liquibase formatted sql

--changeset tyzhprogramist:21-3
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