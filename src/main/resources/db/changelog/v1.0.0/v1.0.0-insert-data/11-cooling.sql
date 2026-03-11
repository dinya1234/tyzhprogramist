--liquibase formatted sql

--changeset tyzhprogramist:21-9
-- ==================== СИСТЕМЫ ОХЛАЖДЕНИЯ ====================
-- Воздушное охлаждение
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-001', id, 'DeepCool AK400', 'deepcool-ak400', 'Кулер DeepCool AK400, 4 теплотрубки, 120mm вентилятор', 2400.00, 25, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-002', id, 'DeepCool AK500', 'deepcool-ak500', 'Кулер DeepCool AK500, 5 теплотрубок, 120mm вентилятор', 3500.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-003', id, 'DeepCool AK620', 'deepcool-ak620', 'Кулер DeepCool AK620, двухбашенный, 6 теплотрубок, 2x120mm', 4800.00, 18, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-004', id, 'DeepCool AG400', 'deepcool-ag400', 'Кулер DeepCool AG400, 4 теплотрубки, 120mm ARGB', 2300.00, 25, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-005', id, 'DeepCool AG620', 'deepcool-ag620', 'Кулер DeepCool AG620, двухбашенный, 6 теплотрубок, 2x120mm ARGB', 5200.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-006', id, 'Noctua NH-D15', 'noctua-nh-d15', 'Кулер Noctua NH-D15, двухбашенный, 6 теплотрубок, 2x140mm', 9500.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-007', id, 'Noctua NH-U12S', 'noctua-nh-u12s', 'Кулер Noctua NH-U12S, 4 теплотрубки, 120mm', 5500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-008', id, 'be quiet! Dark Rock 4', 'bequiet-dark-rock-4', 'Кулер be quiet! Dark Rock 4, 6 теплотрубок, 135mm', 6200.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-009', id, 'be quiet! Dark Rock Pro 4', 'bequiet-dark-rock-pro-4', 'Кулер be quiet! Dark Rock Pro 4, двухбашенный, 7 теплотрубок, 135+120mm', 8200.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-010', id, 'be quiet! Pure Rock 2', 'bequiet-pure-rock-2', 'Кулер be quiet! Pure Rock 2, 4 теплотрубки, 120mm', 3200.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-011', id, 'ID-Cooling SE-214-XT', 'idcooling-se-214-xt', 'Кулер ID-Cooling SE-214-XT, 4 теплотрубки, 120mm', 1800.00, 30, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-012', id, 'ID-Cooling SE-224-XTS', 'idcooling-se-224-xts', 'Кулер ID-Cooling SE-224-XTS, 4 теплотрубки, 120mm ARGB', 2100.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-013', id, 'ID-Cooling SE-226-XT', 'idcooling-se-226-xt', 'Кулер ID-Cooling SE-226-XT, 6 теплотрубок, 120mm', 2800.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIR-014', id, 'Thermalright Peerless Assassin 120', 'thermalright-peerless-assassin-120', 'Кулер Thermalright Peerless Assassin 120, двухбашенный, 7 теплотрубок, 2x120mm', 3500.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

-- Жидкостное охлаждение
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-001', id, 'DeepCool LS520', 'deepcool-ls520', 'СЖО DeepCool LS520, 240мм, ARGB', 6800.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-002', id, 'DeepCool LS720', 'deepcool-ls720', 'СЖО DeepCool LS720, 360мм, ARGB', 8500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-003', id, 'DeepCool LT520', 'deepcool-lt520', 'СЖО DeepCool LT520, 240мм, ARGB', 7200.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-004', id, 'DeepCool LT720', 'deepcool-lt720', 'СЖО DeepCool LT720, 360мм, ARGB', 9500.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-005', id, 'Corsair iCUE H100i', 'corsair-icue-h100i', 'СЖО Corsair iCUE H100i RGB, 240мм', 9500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-006', id, 'Corsair iCUE H150i', 'corsair-icue-h150i', 'СЖО Corsair iCUE H150i RGB, 360мм', 12500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-007', id, 'Corsair iCUE H170i', 'corsair-icue-h170i', 'СЖО Corsair iCUE H170i RGB, 420мм', 16500.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-008', id, 'MSI MAG CoreLiquid 240R', 'msi-mag-coreliquid-240r', 'СЖО MSI MAG CoreLiquid 240R, 240мм', 6500.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-009', id, 'MSI MPG CoreLiquid K360', 'msi-mpg-coreliquid-k360', 'СЖО MSI MPG CoreLiquid K360, 360мм, дисплей', 16500.00, 6, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-010', id, 'NZXT Kraken 240', 'nzxt-kraken-240', 'СЖО NZXT Kraken 240 RGB, 240мм', 9500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-011', id, 'NZXT Kraken 360', 'nzxt-kraken-360', 'СЖО NZXT Kraken 360 RGB, 360мм', 12500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-012', id, 'be quiet! Silent Loop 2 240', 'bequiet-silent-loop-2-240', 'СЖО be quiet! Silent Loop 2 240мм', 8500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'COOLER-AIO-013', id, 'be quiet! Silent Loop 2 360', 'bequiet-silent-loop-2-360', 'СЖО be quiet! Silent Loop 2 360мм', 11500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cooling';