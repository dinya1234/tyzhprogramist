--liquibase formatted sql

--changeset tyzhprogramist:21-5
-- ==================== ВИДЕОКАРТЫ AMD ====================
-- RX 6000 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-001', id, 'AMD Radeon RX 6400', 'amd-rx-6400', 'Видеокарта AMD Radeon RX 6400 4GB GDDR6', 12000.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

-- ... (все карты AMD) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-018', id, 'AMD Radeon RX 7900 XTX', 'amd-rx-7900-xtx', 'Видеокарта AMD Radeon RX 7900 XTX 24GB GDDR6', 92500.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';