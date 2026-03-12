--liquibase formatted sql

--changeset tyzhprogramist:21-2
-- ==================== МАТЕРИНСКИЕ ПЛАТЫ ====================
-- Socket AM4
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-001', id, 'MSI B550-A PRO', 'msi-b550-a-pro', 'Материнская плата MSI B550-A PRO, Socket AM4, ATX', 10500.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-002', id, 'ASUS TUF GAMING B550-PLUS', 'asus-tuf-b550-plus', 'Материнская плата ASUS TUF GAMING B550-PLUS, Socket AM4, ATX', 12000.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-003', id, 'Gigabyte B550 AORUS ELITE', 'gigabyte-b550-aorus-elite', 'Материнская плата Gigabyte B550 AORUS ELITE V2, Socket AM4, ATX', 11500.00, 7, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-004', id, 'ASRock B550 Steel Legend', 'asrock-b550-steel-legend', 'Материнская плата ASRock B550 Steel Legend, Socket AM4, ATX', 13000.00, 5, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-005', id, 'MSI MAG B550 TOMAHAWK', 'msi-mag-b550-tomahawk', 'Материнская плата MSI MAG B550 TOMAHAWK, Socket AM4, ATX', 14000.00, 5, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-006', id, 'ASUS ROG STRIX B550-F', 'asus-rog-strix-b550-f', 'Материнская плата ASUS ROG STRIX B550-F GAMING, Socket AM4, ATX', 15500.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-007', id, 'MSI X570-A PRO', 'msi-x570-a-pro', 'Материнская плата MSI X570-A PRO, Socket AM4, ATX', 13500.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-008', id, 'ASUS ROG CROSSHAIR VIII DARK HERO', 'asus-crosshair-viii-dark-hero', 'Материнская плата ASUS ROG CROSSHAIR VIII DARK HERO, Socket AM4, ATX', 32500.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

-- Socket AM5
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-009', id, 'MSI B650 GAMING PLUS', 'msi-b650-gaming-plus', 'Материнская плата MSI B650 GAMING PLUS, Socket AM5, ATX', 15500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-010', id, 'ASUS TUF GAMING B650-PLUS', 'asus-tuf-b650-plus', 'Материнская плата ASUS TUF GAMING B650-PLUS, Socket AM5, ATX', 17000.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-011', id, 'Gigabyte B650 AORUS ELITE', 'gigabyte-b650-aorus-elite', 'Материнская плата Gigabyte B650 AORUS ELITE, Socket AM5, ATX', 17500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-012', id, 'ASRock B650 Steel Legend', 'asrock-b650-steel-legend', 'Материнская плата ASRock B650 Steel Legend, Socket AM5, ATX', 18500.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-013', id, 'MSI MPG B650 CARBON', 'msi-mpg-b650-carbon', 'Материнская плата MSI MPG B650 CARBON WIFI, Socket AM5, ATX', 24500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-014', id, 'ASUS ROG STRIX B650E-F', 'asus-rog-strix-b650e-f', 'Материнская плата ASUS ROG STRIX B650E-F GAMING WIFI, Socket AM5, ATX', 22500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-015', id, 'MSI X670E CARBON', 'msi-x670e-carbon', 'Материнская плата MSI MPG X670E CARBON WIFI, Socket AM5, ATX', 34500.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-016', id, 'ASUS ROG CROSSHAIR X670E HERO', 'asus-crosshair-x670e-hero', 'Материнская плата ASUS ROG CROSSHAIR X670E HERO, Socket AM5, ATX', 52500.00, 2, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

-- Socket LGA1700
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-017', id, 'MSI PRO B660-A', 'msi-pro-b660-a', 'Материнская плата MSI PRO B660-A, LGA 1700, ATX', 9500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-018', id, 'Gigabyte B760 DS3H', 'gigabyte-b760-ds3h', 'Материнская плата Gigabyte B760 DS3H, LGA 1700, ATX', 10500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-019', id, 'MSI B760 GAMING PLUS', 'msi-b760-gaming-plus', 'Материнская плата MSI B760 GAMING PLUS WIFI, LGA 1700, ATX', 13500.00, 8, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-020', id, 'ASUS TUF GAMING B760-PLUS', 'asus-tuf-b760-plus', 'Материнская плата ASUS TUF GAMING B760-PLUS WIFI, LGA 1700, ATX', 15500.00, 7, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-021', id, 'MSI Z790 GAMING', 'msi-z790-gaming', 'Материнская плата MSI Z790 GAMING PLUS WIFI, LGA 1700, ATX', 18500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-022', id, 'ASUS ROG STRIX Z790-E', 'asus-rog-strix-z790-e', 'Материнская плата ASUS ROG STRIX Z790-E GAMING WIFI, LGA 1700, ATX', 28500.00, 4, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-023', id, 'Gigabyte Z790 AORUS MASTER', 'gigabyte-z790-aorus-master', 'Материнская плата Gigabyte Z790 AORUS MASTER, LGA 1700, ATX', 37500.00, 3, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-024', id, 'MSI MEG Z790 ACE', 'msi-meg-z790-ace', 'Материнская плата MSI MEG Z790 ACE, LGA 1700, ATX', 46500.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';