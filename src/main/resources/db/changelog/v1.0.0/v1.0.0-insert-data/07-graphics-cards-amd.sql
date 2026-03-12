--liquibase formatted sql

--changeset tyzhprogramist:21-5
-- ==================== ВИДЕОКАРТЫ AMD ====================
-- RX 6000 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-001', id, 'AMD Radeon RX 6400', 'amd-rx-6400', 'Видеокарта AMD Radeon RX 6400 4GB GDDR6', 12000.00, 10, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-002', id, 'AMD Radeon RX 6500 XT', 'amd-rx-6500-xt', 'Видеокарта AMD Radeon RX 6500 XT 4GB GDDR6', 15500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-003', id, 'AMD Radeon RX 6600', 'amd-rx-6600', 'Видеокарта AMD Radeon RX 6600 8GB GDDR6', 24000.00, 12, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-004', id, 'AMD Radeon RX 6600 XT', 'amd-rx-6600-xt', 'Видеокарта AMD Radeon RX 6600 XT 8GB GDDR6', 28000.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-005', id, 'AMD Radeon RX 6650 XT', 'amd-rx-6650-xt', 'Видеокарта AMD Radeon RX 6650 XT 8GB GDDR6', 29500.00, 10, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-006', id, 'AMD Radeon RX 6700 XT', 'amd-rx-6700-xt', 'Видеокарта AMD Radeon RX 6700 XT 12GB GDDR6', 33500.00, 7, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-007', id, 'AMD Radeon RX 6750 XT', 'amd-rx-6750-xt', 'Видеокарта AMD Radeon RX 6750 XT 12GB GDDR6', 36500.00, 6, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-008', id, 'AMD Radeon RX 6800', 'amd-rx-6800', 'Видеокарта AMD Radeon RX 6800 16GB GDDR6', 45000.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-009', id, 'AMD Radeon RX 6800 XT', 'amd-rx-6800-xt', 'Видеокарта AMD Radeon RX 6800 XT 16GB GDDR6', 52000.00, 3, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-010', id, 'AMD Radeon RX 6900 XT', 'amd-rx-6900-xt', 'Видеокарта AMD Radeon RX 6900 XT 16GB GDDR6', 65000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-011', id, 'AMD Radeon RX 6950 XT', 'amd-rx-6950-xt', 'Видеокарта AMD Radeon RX 6950 XT 16GB GDDR6', 72000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

-- RX 7000 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-012', id, 'AMD Radeon RX 7600', 'amd-rx-7600', 'Видеокарта AMD Radeon RX 7600 8GB GDDR6', 26500.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-013', id, 'AMD Radeon RX 7600 XT', 'amd-rx-7600-xt', 'Видеокарта AMD Radeon RX 7600 XT 16GB GDDR6', 32500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-014', id, 'AMD Radeon RX 7700 XT', 'amd-rx-7700-xt', 'Видеокарта AMD Radeon RX 7700 XT 12GB GDDR6', 42500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-015', id, 'AMD Radeon RX 7800 XT', 'amd-rx-7800-xt', 'Видеокарта AMD Radeon RX 7800 XT 16GB GDDR6', 52500.00, 5, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-016', id, 'AMD Radeon RX 7900 GRE', 'amd-rx-7900-gre', 'Видеокарта AMD Radeon RX 7900 GRE 16GB GDDR6', 59500.00, 4, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-017', id, 'AMD Radeon RX 7900 XT', 'amd-rx-7900-xt', 'Видеокарта AMD Radeon RX 7900 XT 20GB GDDR6', 75000.00, 3, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-AMD-018', id, 'AMD Radeon RX 7900 XTX', 'amd-rx-7900-xtx', 'Видеокарта AMD Radeon RX 7900 XTX 24GB GDDR6', 92500.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';