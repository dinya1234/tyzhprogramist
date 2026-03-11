--liquibase formatted sql

--changeset tyzhprogramist:21-9
-- ==================== СИСТЕМЫ ОХЛАЖДЕНИЯ ====================
-- Воздушное охлаждение
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-001', id, 'DeepCool AK400', 'deepcool-ak400', 'Кулер DeepCool AK400, 4 теплотрубки, 120mm вентилятор', 2400.00, 25, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

-- ... (все кулеры и СЖО) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-013', id, 'be quiet! Silent Loop 2 360', 'bequiet-silent-loop-2-360', 'СЖО be quiet! Silent Loop 2 360мм', 11500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';