--liquibase formatted sql

--changeset tyzhprogramist:23-1
-- Insert new peripheral categories
INSERT INTO categories (name, slug, sort_order) VALUES
('Клавиатуры', 'keyboards', 9),
('Мыши', 'mice', 10),
('Коврики для мыши', 'mouse-pads', 11);

-- ==================== КЛАВИАТУРЫ ====================
-- Механические клавиатуры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-001', id, 'Logitech G Pro X Mechanical', 'logitech-g-pro-x-mech', 'Механическая клавиатура Logitech G Pro X, Hot-Swap переключатели, RGB', 14500.00, 8, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'keyboards';

-- ... (все клавиатуры) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-005', id, 'Apple Magic Keyboard', 'apple-magic-keyboard', 'Apple Magic Keyboard, беспроводная, для Mac', 10500.00, 7, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

-- ==================== МЫШИ ====================
-- Игровые мыши
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-001', id, 'Logitech G PRO X Superlight', 'logitech-g-pro-x-superlight', 'Игровая мышь Logitech G PRO X Superlight, 25000 DPI, 63g, беспроводная', 13500.00, 10, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'mice';

-- ... (все мыши) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-004', id, 'Microsoft Bluetooth Mouse', 'microsoft-bluetooth-mouse', 'Microsoft Bluetooth Mouse, эргономичная', 2800.00, 15, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'mice';

-- ==================== КОВРИКИ ДЛЯ МЫШИ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-001', id, 'Razer Strider XXL', 'razer-strider-xxl', 'Игровой коврик Razer Strider, размер XXL, 940x410x3мм', 3500.00, 15, NOW(), TRUE, TRUE, TRUE, 12
FROM categories WHERE slug = 'mouse-pads';

-- ... (все коврики) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-005', id, 'Corsair MM350', 'corsair-mm350', 'Игровой коврик Corsair MM350 Champion Series, XL', 3200.00, 10, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mouse-pads';