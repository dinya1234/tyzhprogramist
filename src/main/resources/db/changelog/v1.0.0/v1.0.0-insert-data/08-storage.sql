--liquibase formatted sql

--changeset tyzhprogramist:21-6
-- ==================== НАКОПИТЕЛИ ====================
-- SATA SSD
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-001', id, 'Samsung 870 EVO 250GB', 'samsung-870-evo-250gb', 'SSD накопитель Samsung 870 EVO 250GB SATA III', 3500.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

-- ... (все SSD и HDD) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-007', id, 'Toshiba P300 2TB', 'toshiba-p300-2tb', 'Жесткий диск Toshiba P300 2TB 7200rpm', 5400.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';