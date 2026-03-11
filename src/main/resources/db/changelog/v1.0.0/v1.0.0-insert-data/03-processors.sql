--liquibase formatted sql

--changeset tyzhprogramist:21-1
-- ==================== ПРОЦЕССОРЫ INTEL ====================
-- Intel Core i3
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-001', id, 'Intel Core i3-12100F', 'intel-core-i3-12100f', 'Процессор Intel Core i3-12100F, LGA 1700, 4 ядра, 8 потоков, до 4.3 ГГц', 8500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- ... (и так далее, все процессоры Intel из вашего файла) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-INTEL-012', id, 'Intel Core i9-14900K', 'intel-core-i9-14900k', 'Процессор Intel Core i9-14900K, LGA 1700, 24 ядра (8P+16E), 32 потока, до 6.0 ГГц', 65000.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'processory';

-- ==================== ПРОЦЕССОРЫ AMD ====================
-- AMD Ryzen 3
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-001', id, 'AMD Ryzen 3 4100', 'amd-ryzen-3-4100', 'Процессор AMD Ryzen 3 4100, AM4, 4 ядра, 8 потоков, до 4.0 ГГц', 6500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'processory';

-- ... (и так далее, все процессоры AMD) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'CPU-AMD-014', id, 'AMD Ryzen 9 7950X3D', 'amd-ryzen-9-7950x3d', 'Процессор AMD Ryzen 9 7950X3D, AM5, 16 ядер, 32 потока, до 5.7 ГГц, 3D V-Cache', 59500.00, 2, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'processory';