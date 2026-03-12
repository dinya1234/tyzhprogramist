--liquibase formatted sql

--changeset tyzhprogramist:23-3
-- Insert new peripheral categories
INSERT INTO categories (name, slug, sort_order) VALUES
('Сетевое оборудование', 'networking', 15);

-- ==================== СЕТЕВОЕ ОБОРУДОВАНИЕ ====================
-- Роутеры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-001', id, 'TP-Link Archer AX73', 'tp-link-archer-ax73', 'Wi-Fi роутер TP-Link Archer AX73, AX5400, гигабитный', 7500.00, 12, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-002', id, 'TP-Link Archer AX11000', 'tp-link-archer-ax11000', 'Wi-Fi роутер TP-Link Archer AX11000, три-диапазонный', 23500.00, 4, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-003', id, 'ASUS RT-AX82U', 'asus-rt-ax82u', 'Wi-Fi роутер ASUS RT-AX82U, AX5400, RGB', 12500.00, 8, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-004', id, 'ASUS ROG Rapture GT-AX11000', 'asus-rog-rapture-gt-ax11000', 'Игровой роутер ASUS ROG Rapture GT-AX11000', 32500.00, 3, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-RTR-005', id, 'MikroTik hAP ac2', 'mikrotik-hap-ac2', 'Роутер MikroTik hAP ac2, гигабитный', 5500.00, 10, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';

-- Сетевые карты и адаптеры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-ADP-001', id, 'TP-Link Archer TX50E', 'tp-link-archer-tx50e', 'PCIe адаптер TP-Link Archer TX50E, Wi-Fi 6, Bluetooth 5.0', 4200.00, 10, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-ADP-002', id, 'ASUS PCE-AX58BT', 'asus-pce-ax58bt', 'PCIe адаптер ASUS PCE-AX58BT, Wi-Fi 6, Bluetooth 5.0', 4800.00, 8, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-ADP-003', id, 'TP-Link Archer T3U', 'tp-link-archer-t3u', 'USB адаптер TP-Link Archer T3U, Wi-Fi AC1300', 1500.00, 20, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';

-- Коммутаторы
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-SW-001', id, 'TP-Link TL-SG105', 'tp-link-tl-sg105', 'Коммутатор TP-Link TL-SG105, 5 портов гигабитный', 1500.00, 15, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-SW-002', id, 'TP-Link TL-SG108', 'tp-link-tl-sg108', 'Коммутатор TP-Link TL-SG108, 8 портов гигабитный', 2200.00, 12, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-SW-003', id, 'MikroTik CRS112-8P-4S-IN', 'mikrotik-crs112-8p-4s-in', 'Коммутатор MikroTik CRS112-8P-4S-IN, 8 портов PoE', 11500.00, 5, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';