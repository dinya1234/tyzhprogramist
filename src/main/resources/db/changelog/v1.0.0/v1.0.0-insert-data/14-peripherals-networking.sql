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

-- ... (все сетевое оборудование) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'NET-SW-003', id, 'MikroTik CRS112-8P-4S-IN', 'mikrotik-crs112-8p-4s-in', 'Коммутатор MikroTik CRS112-8P-4S-IN, 8 портов PoE', 11500.00, 5, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'networking';