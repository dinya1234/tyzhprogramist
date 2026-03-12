--liquibase formatted sql

--changeset tyzhprogramist:21-1
-- ==================== ПРОЦЕССОРЫ INTEL ====================
-- Intel Core i3
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-001', id, 'Intel Core i3-12100F', 'intel-core-i3-12100f', 'Процессор Intel Core i3-12100F, LGA 1700, 4 ядра, 8 потоков, до 4.3 ГГц', 8500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-002', id, 'Intel Core i3-13100', 'intel-core-i3-13100', 'Процессор Intel Core i3-13100, LGA 1700, 4 ядра, 8 потоков, до 4.5 ГГц, встроенная графика', 11500.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

-- Intel Core i5
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-003', id, 'Intel Core i5-12400F', 'intel-core-i5-12400f', 'Процессор Intel Core i5-12400F, LGA 1700, 6 ядер, 12 потоков, до 4.4 ГГц', 15000.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-004', id, 'Intel Core i5-12600K', 'intel-core-i5-12600k', 'Процессор Intel Core i5-12600K, LGA 1700, 10 ядер (6P+4E), 16 потоков, до 4.9 ГГц', 22000.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-005', id, 'Intel Core i5-13400F', 'intel-core-i5-13400f', 'Процессор Intel Core i5-13400F, LGA 1700, 10 ядер (6P+4E), 16 потоков, до 4.6 ГГц', 19000.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-006', id, 'Intel Core i5-13600K', 'intel-core-i5-13600k', 'Процессор Intel Core i5-13600K, LGA 1700, 14 ядер (6P+8E), 20 потоков, до 5.1 ГГц', 28500.00, 7, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

-- Intel Core i7
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-007', id, 'Intel Core i7-12700K', 'intel-core-i7-12700k', 'Процессор Intel Core i7-12700K, LGA 1700, 12 ядер (8P+4E), 20 потоков, до 5.0 ГГц', 29000.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-008', id, 'Intel Core i7-13700K', 'intel-core-i7-13700k', 'Процессор Intel Core i7-13700K, LGA 1700, 16 ядер (8P+8E), 24 потока, до 5.4 ГГц', 37500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-009', id, 'Intel Core i7-14700K', 'intel-core-i7-14700k', 'Процессор Intel Core i7-14700K, LGA 1700, 20 ядер (8P+12E), 28 потоков, до 5.6 ГГц', 42500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- Intel Core i9
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-010', id, 'Intel Core i9-12900K', 'intel-core-i9-12900k', 'Процессор Intel Core i9-12900K, LGA 1700, 16 ядер (8P+8E), 24 потока, до 5.2 ГГц', 45000.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-011', id, 'Intel Core i9-13900K', 'intel-core-i9-13900k', 'Процессор Intel Core i9-13900K, LGA 1700, 24 ядра (8P+16E), 32 потока, до 5.8 ГГц', 58000.00, 3, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-012', id, 'Intel Core i9-14900K', 'intel-core-i9-14900k', 'Процессор Intel Core i9-14900K, LGA 1700, 24 ядра (8P+16E), 32 потока, до 6.0 ГГц', 65000.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- ==================== ПРОЦЕССОРЫ AMD ====================
-- AMD Ryzen 3
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-001', id, 'AMD Ryzen 3 4100', 'amd-ryzen-3-4100', 'Процессор AMD Ryzen 3 4100, AM4, 4 ядра, 8 потоков, до 4.0 ГГц', 6500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-002', id, 'AMD Ryzen 3 5300G', 'amd-ryzen-3-5300g', 'Процессор AMD Ryzen 3 5300G, AM4, 4 ядра, 8 потоков, до 4.2 ГГц, встроенная графика', 9500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- AMD Ryzen 5
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-003', id, 'AMD Ryzen 5 4500', 'amd-ryzen-5-4500', 'Процессор AMD Ryzen 5 4500, AM4, 6 ядер, 12 потоков, до 4.1 ГГц', 9000.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-004', id, 'AMD Ryzen 5 5600X', 'amd-ryzen-5-5600x', 'Процессор AMD Ryzen 5 5600X, AM4, 6 ядер, 12 потоков, до 4.6 ГГц', 18000.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-005', id, 'AMD Ryzen 5 7500F', 'amd-ryzen-5-7500f', 'Процессор AMD Ryzen 5 7500F, AM5, 6 ядер, 12 потоков, до 5.0 ГГц', 16500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-006', id, 'AMD Ryzen 5 7600X', 'amd-ryzen-5-7600x', 'Процессор AMD Ryzen 5 7600X, AM5, 6 ядер, 12 потоков, до 5.3 ГГц', 20500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- AMD Ryzen 7
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-007', id, 'AMD Ryzen 7 5700X', 'amd-ryzen-7-5700x', 'Процессор AMD Ryzen 7 5700X, AM4, 8 ядер, 16 потоков, до 4.6 ГГц', 19000.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-008', id, 'AMD Ryzen 7 5800X3D', 'amd-ryzen-7-5800x3d', 'Процессор AMD Ryzen 7 5800X3D, AM4, 8 ядер, 16 потоков, до 4.5 ГГц, 3D V-Cache', 28000.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-009', id, 'AMD Ryzen 7 7700X', 'amd-ryzen-7-7700x', 'Процессор AMD Ryzen 7 7700X, AM5, 8 ядер, 16 потоков, до 5.4 ГГц', 26500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-010', id, 'AMD Ryzen 7 7800X3D', 'amd-ryzen-7-7800x3d', 'Процессор AMD Ryzen 7 7800X3D, AM5, 8 ядер, 16 потоков, до 5.0 ГГц, 3D V-Cache', 36500.00, 4, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';

-- AMD Ryzen 9
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-011', id, 'AMD Ryzen 9 5900X', 'amd-ryzen-9-5900x', 'Процессор AMD Ryzen 9 5900X, AM4, 12 ядер, 24 потока, до 4.8 ГГц', 29500.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-012', id, 'AMD Ryzen 9 5950X', 'amd-ryzen-9-5950x', 'Процессор AMD Ryzen 9 5950X, AM4, 16 ядер, 32 потока, до 4.9 ГГц', 42500.00, 3, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-013', id, 'AMD Ryzen 9 7900X', 'amd-ryzen-9-7900x', 'Процессор AMD Ryzen 9 7900X, AM5, 12 ядер, 24 потока, до 5.6 ГГц', 39500.00, 3, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-014', id, 'AMD Ryzen 9 7950X3D', 'amd-ryzen-9-7950x3d', 'Процессор AMD Ryzen 9 7950X3D, AM5, 16 ядер, 32 потока, до 5.7 ГГц, 3D V-Cache', 59500.00, 2, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';