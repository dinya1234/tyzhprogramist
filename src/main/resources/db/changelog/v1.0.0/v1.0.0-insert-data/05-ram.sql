--liquibase formatted sql

--changeset tyzhprogramist:21-3
-- ==================== ОПЕРАТИВНАЯ ПАМЯТЬ ====================
-- DDR4
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR4-001', id, 'Kingston Fury 8GB DDR4', 'kingston-fury-8gb-ddr4', 'Оперативная память Kingston Fury 8GB DDR4 3200MHz', 2800.00, 30, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'ram';

-- ... (вся DDR4 и DDR5) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'RAM-DDR5-010', id, 'ADATA XPG Lancer 32GB DDR5', 'adata-xpg-lancer-32gb-ddr5', 'Оперативная память ADATA XPG Lancer 32GB DDR5 6000MHz', 11500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'ram';