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

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-002', id, 'Razer BlackWidow V4', 'razer-blackwidow-v4', 'Механическая клавиатура Razer BlackWidow V4, Green Switches, RGB', 16500.00, 6, NOW(), TRUE, TRUE, FALSE, 24
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-003', id, 'Corsair K70 RGB PRO', 'corsair-k70-rgb-pro', 'Механическая клавиатура Corsair K70 RGB PRO, Cherry MX Red, RGB', 15500.00, 7, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-004', id, 'Keychron K8 Pro', 'keychron-k8-pro', 'Механическая клавиатура Keychron K8 Pro, Gateron G Pro, QMK/VIA, Bluetooth', 9500.00, 5, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MECH-005', id, 'Ducky One 3 Mini', 'ducky-one-3-mini', 'Механическая клавиатура Ducky One 3 Mini 60%, Cherry MX Speed Silver', 11500.00, 4, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'keyboards';

-- Мембранные клавиатуры
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-001', id, 'Logitech MK270', 'logitech-mk270', 'Комплект клавиатуры и мыши Logitech MK270, беспроводной', 3500.00, 20, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-002', id, 'Logitech K380', 'logitech-k380', 'Компактная Bluetooth клавиатура Logitech K380, мультидевайс', 4200.00, 12, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-003', id, 'Logitech MX Keys', 'logitech-mx-keys', 'Беспроводная клавиатура Logitech MX Keys, подсветка, для работы', 9500.00, 8, NOW(), TRUE, FALSE, TRUE, 24
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-004', id, 'Microsoft Ergonomic', 'microsoft-ergonomic', 'Эргономичная клавиатура Microsoft Ergonomic, USB', 6800.00, 6, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'KB-MEM-005', id, 'Apple Magic Keyboard', 'apple-magic-keyboard', 'Apple Magic Keyboard, беспроводная, для Mac', 10500.00, 7, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'keyboards';

-- ==================== МЫШИ ====================
-- Игровые мыши
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-001', id, 'Logitech G PRO X Superlight', 'logitech-g-pro-x-superlight', 'Игровая мышь Logitech G PRO X Superlight, 25000 DPI, 63g, беспроводная', 13500.00, 10, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-002', id, 'Razer DeathAdder V3 Pro', 'razer-deathadder-v3-pro', 'Игровая мышь Razer DeathAdder V3 Pro, 30000 DPI, беспроводная', 12500.00, 8, NOW(), TRUE, TRUE, FALSE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-003', id, 'Razer Viper V2 Pro', 'razer-viper-v2-pro', 'Игровая мышь Razer Viper V2 Pro, 30000 DPI, 58g, беспроводная', 12500.00, 7, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-004', id, 'Logitech G502 X PLUS', 'logitech-g502-x-plus', 'Игровая мышь Logitech G502 X PLUS, LIGHTFORCE, беспроводная, RGB', 11500.00, 9, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-005', id, 'SteelSeries Aerox 5', 'steelseries-aerox-5', 'Игровая мышь SteelSeries Aerox 5 Wireless, 18000 DPI, ультралегкая', 9500.00, 6, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-006', id, 'Zowie EC2-C', 'zowie-ec2-c', 'Игровая мышь Zowie EC2-C, 3200 DPI, проводная, для киберспорта', 6800.00, 5, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-GAME-007', id, 'Corsair Sabre RGB Pro', 'corsair-sabre-rgb-pro', 'Игровая мышь Corsair Sabre RGB Pro, проводная, 18000 DPI', 5500.00, 8, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mice';

-- Офисные мыши
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-001', id, 'Logitech MX Master 3S', 'logitech-mx-master-3s', 'Беспроводная мышь Logitech MX Master 3S, 8000 DPI, для работы', 9500.00, 12, NOW(), TRUE, TRUE, TRUE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-002', id, 'Logitech MX Anywhere 3S', 'logitech-mx-anywhere-3s', 'Компактная мышь Logitech MX Anywhere 3S, беспроводная', 7500.00, 10, NOW(), TRUE, TRUE, FALSE, 24
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-003', id, 'Logitech M185', 'logitech-m185', 'Беспроводная мышь Logitech M185, компактная', 1200.00, 30, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'mice';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'MOUSE-OFFICE-004', id, 'Microsoft Bluetooth Mouse', 'microsoft-bluetooth-mouse', 'Microsoft Bluetooth Mouse, эргономичная', 2800.00, 15, NOW(), TRUE, FALSE, FALSE, 12
FROM categories WHERE slug = 'mice';

-- ==================== КОВРИКИ ДЛЯ МЫШИ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-001', id, 'Razer Strider XXL', 'razer-strider-xxl', 'Игровой коврик Razer Strider, размер XXL, 940x410x3мм', 3500.00, 15, NOW(), TRUE, TRUE, TRUE, 12
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-002', id, 'Logitech G840', 'logitech-g840', 'Игровой коврик Logitech G840, размер XL, 900x400x3мм', 4500.00, 12, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-003', id, 'SteelSeries QcK Heavy', 'steelseries-qck-heavy', 'Игровой коврик SteelSeries QcK Heavy, размер L, 450x400x6мм', 2800.00, 20, NOW(), TRUE, FALSE, TRUE, 12
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-004', id, 'SteelSeries QcK Prism', 'steelseries-qck-prism', 'Игровой коврик SteelSeries QcK Prism с подсветкой, XL', 5800.00, 8, NOW(), TRUE, TRUE, FALSE, 12
FROM categories WHERE slug = 'mouse-pads';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller, warranty_months)
SELECT 'PAD-005', id, 'Corsair MM350', 'corsair-mm350', 'Игровой коврик Corsair MM350 Champion Series, XL', 3200.00, 10, NOW(), TRUE, FALSE, FALSE, 24
FROM categories WHERE slug = 'mouse-pads';