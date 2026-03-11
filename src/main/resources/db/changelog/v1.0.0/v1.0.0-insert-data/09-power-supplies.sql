--liquibase formatted sql

--changeset tyzhprogramist:21-7
-- ==================== БЛОКИ ПИТАНИЯ ====================
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-001', id, 'Corsair CV550', 'corsair-cv550', 'Блок питания Corsair CV550 550W, 80+ Bronze', 4500.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';

-- ... (все блоки питания) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'PSU-019', id, 'Seasonic Prime TX-1000', 'seasonic-prime-tx-1000', 'Блок питания Seasonic Prime TX-1000 1000W, 80+ Titanium', 22500.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'power-supplies';