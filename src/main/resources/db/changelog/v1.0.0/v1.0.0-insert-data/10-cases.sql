--liquibase formatted sql

--changeset tyzhprogramist:21-8
-- ==================== КОРПУСА ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-001', id, 'DeepCool CC560', 'deepcool-cc560', 'Корпус DeepCool CC560, Midi-Tower, 4 вентилятора', 4500.00, 20, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-002', id, 'DeepCool CC660', 'deepcool-cc660', 'Корпус DeepCool CC660, Midi-Tower, стекло, 3 вентилятора', 5200.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-003', id, 'DeepCool CH370', 'deepcool-ch370', 'Корпус DeepCool CH370, Midi-Tower, белый, стекло', 5800.00, 12, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-004', id, 'DeepCool CH560', 'deepcool-ch560', 'Корпус DeepCool CH560, Midi-Tower, 4 вентилятора, цифровой дисплей', 8500.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-005', id, 'Corsair 4000D', 'corsair-4000d', 'Корпус Corsair 4000D Airflow, Midi-Tower, белый', 8500.00, 12, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-006', id, 'Corsair 4000D RGB', 'corsair-4000d-rgb', 'Корпус Corsair 4000D RGB Airflow, Midi-Tower, 3 вентилятора', 10500.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-007', id, 'Corsair 5000D', 'corsair-5000d', 'Корпус Corsair 5000D Airflow, Midi-Tower, стекло', 12500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-008', id, 'Corsair iCUE 7000X', 'corsair-icue-7000x', 'Корпус Corsair iCUE 7000X RGB, Full-Tower, стекло, 4 вентилятора', 26500.00, 4, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-009', id, 'Zalman S2', 'zalman-s2', 'Корпус Zalman S2, Midi-Tower, 4 вентилятора', 3800.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-010', id, 'Zalman N5', 'zalman-n5', 'Корпус Zalman N5, Midi-Tower, стекло', 4200.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-011', id, 'Zalman i4', 'zalman-i4', 'Корпус Zalman i4, Midi-Tower, RGB, 4 вентилятора', 5200.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-012', id, 'be quiet! Pure Base 500', 'bequiet-pure-base-500', 'Корпус be quiet! Pure Base 500, Midi-Tower, черный', 6200.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-013', id, 'be quiet! Pure Base 500DX', 'bequiet-pure-base-500dx', 'Корпус be quiet! Pure Base 500DX, Midi-Tower, ARGB', 8200.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-014', id, 'be quiet! Dark Base 700', 'bequiet-dark-base-700', 'Корпус be quiet! Dark Base 700, Full-Tower, стекло', 16500.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-015', id, 'Lian Li Lancool 215', 'lianli-lancool-215', 'Корпус Lian Li Lancool 215, Midi-Tower, 2x200mm вентилятора', 7200.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-016', id, 'Lian Li Lancool 216', 'lianli-lancool-216', 'Корпус Lian Li Lancool 216, Midi-Tower, 2x160mm + 1x140mm', 8500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-017', id, 'Lian Li O11 Dynamic', 'lianli-o11-dynamic', 'Корпус Lian Li O11 Dynamic, Midi-Tower, стекло, 3 вентилятора', 11500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-018', id, 'NZXT H5 Flow', 'nzxt-h5-flow', 'Корпус NZXT H5 Flow, Midi-Tower, белый', 7500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-019', id, 'NZXT H7 Flow', 'nzxt-h7-flow', 'Корпус NZXT H7 Flow, Midi-Tower, черный', 9500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'cases';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CASE-020', id, 'NZXT H9 Elite', 'nzxt-h9-elite', 'Корпус NZXT H9 Elite, Dual-Chamber, стекло, 3 вентилятора', 17500.00, 5, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'cases';