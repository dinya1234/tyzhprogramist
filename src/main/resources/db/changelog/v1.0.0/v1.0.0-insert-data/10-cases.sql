--liquibase formatted sql

--changeset tyzhprogramist:21-8
-- ==================== КОРПУСА ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-001', id, 'DeepCool CC560', 'deepcool-cc560', 'Корпус DeepCool CC560, Midi-Tower, 4 вентилятора', 4500.00, 20, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

-- ... (все корпуса) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-020', id, 'NZXT H9 Elite', 'nzxt-h9-elite', 'Корпус NZXT H9 Elite, Dual-Chamber, стекло, 3 вентилятора', 17500.00, 5, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';