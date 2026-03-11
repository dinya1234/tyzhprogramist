--liquibase formatted sql

--changeset tyzhprogramist:21-2
-- ==================== МАТЕРИНСКИЕ ПЛАТЫ ====================
-- Socket AM4
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-001', id, 'MSI B550-A PRO', 'msi-b550-a-pro', 'Материнская плата MSI B550-A PRO, Socket AM4, ATX', 10500.00, 8, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'motherboards';

-- ... (все материнские платы) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'MB-024', id, 'MSI MEG Z790 ACE', 'msi-meg-z790-ace', 'Материнская плата MSI MEG Z790 ACE, LGA 1700, ATX', 46500.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'motherboards';