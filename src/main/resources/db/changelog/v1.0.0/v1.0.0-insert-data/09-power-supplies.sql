--liquibase formatted sql

--changeset tyzhprogramist:21-7
-- ==================== БЛОКИ ПИТАНИЯ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-001', id, 'Corsair CV550', 'corsair-cv550', 'Блок питания Corsair CV550 550W, 80+ Bronze', 4500.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-002', id, 'Corsair CV650', 'corsair-cv650', 'Блок питания Corsair CV650 650W, 80+ Bronze', 5200.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-003', id, 'Corsair CX650F', 'corsair-cx650f', 'Блок питания Corsair CX650F RGB 650W, 80+ Bronze', 6500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-004', id, 'Corsair RM650', 'corsair-rm650', 'Блок питания Corsair RM650 650W, 80+ Gold', 8200.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-005', id, 'Corsair RM750', 'corsair-rm750', 'Блок питания Corsair RM750 750W, 80+ Gold', 9500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-006', id, 'Corsair RM850', 'corsair-rm850', 'Блок питания Corsair RM850 850W, 80+ Gold', 11200.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-007', id, 'Corsair RM1000e', 'corsair-rm1000e', 'Блок питания Corsair RM1000e 1000W, 80+ Gold', 14500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-008', id, 'Corsair HX1000', 'corsair-hx1000', 'Блок питания Corsair HX1000 1000W, 80+ Platinum', 18500.00, 4, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-009', id, 'Chieftec GPS-500', 'chieftec-gps-500', 'Блок питания Chieftec GPS-500A8 500W', 3500.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-010', id, 'Chieftec GPS-600', 'chieftec-gps-600', 'Блок питания Chieftec GPS-600A8 600W', 4200.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-011', id, 'be quiet! System Power 9 500W', 'bequiet-system-power-500', 'Блок питания be quiet! System Power 9 500W', 4800.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-012', id, 'be quiet! Pure Power 11 600W', 'bequiet-pure-power-600', 'Блок питания be quiet! Pure Power 11 600W, 80+ Gold', 6500.00, 12, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-013', id, 'be quiet! Straight Power 11 850W', 'bequiet-straight-power-850', 'Блок питания be quiet! Straight Power 11 850W, 80+ Platinum', 12500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-014', id, 'DeepCool PF500', 'deepcool-pf500', 'Блок питания DeepCool PF500 500W', 3200.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-015', id, 'DeepCool PQ750M', 'deepcool-pq750m', 'Блок питания DeepCool PQ750M 750W, 80+ Gold', 8500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-016', id, 'DeepCool PX1000G', 'deepcool-px1000g', 'Блок питания DeepCool PX1000G 1000W, 80+ Gold', 14500.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-017', id, 'Seasonic Focus GX-650', 'seasonic-focus-gx-650', 'Блок питания Seasonic Focus GX-650 650W, 80+ Gold', 9500.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-018', id, 'Seasonic Focus GX-750', 'seasonic-focus-gx-750', 'Блок питания Seasonic Focus GX-750 750W, 80+ Gold', 10500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-019', id, 'Seasonic Prime TX-1000', 'seasonic-prime-tx-1000', 'Блок питания Seasonic Prime TX-1000 1000W, 80+ Titanium', 22500.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';