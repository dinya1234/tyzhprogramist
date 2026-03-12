--liquibase formatted sql

--changeset tyzhprogramist:21-4
-- ==================== ВИДЕОКАРТЫ NVIDIA ====================
-- RTX 30 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-001', id, 'NVIDIA RTX 3050', 'nvidia-rtx-3050', 'Видеокарта NVIDIA GeForce RTX 3050 8GB GDDR6', 23000.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-002', id, 'NVIDIA RTX 3060 12GB', 'nvidia-rtx-3060-12gb', 'Видеокарта NVIDIA GeForce RTX 3060 12GB GDDR6', 32000.00, 10, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-003', id, 'NVIDIA RTX 3060 Ti', 'nvidia-rtx-3060-ti', 'Видеокарта NVIDIA GeForce RTX 3060 Ti 8GB GDDR6', 36000.00, 7, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-004', id, 'NVIDIA RTX 3070', 'nvidia-rtx-3070', 'Видеокарта NVIDIA GeForce RTX 3070 8GB GDDR6', 48000.00, 5, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-005', id, 'NVIDIA RTX 3070 Ti', 'nvidia-rtx-3070-ti', 'Видеокарта NVIDIA GeForce RTX 3070 Ti 8GB GDDR6X', 52000.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-006', id, 'NVIDIA RTX 3080', 'nvidia-rtx-3080', 'Видеокарта NVIDIA GeForce RTX 3080 10GB GDDR6X', 65000.00, 3, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-007', id, 'NVIDIA RTX 3080 Ti', 'nvidia-rtx-3080-ti', 'Видеокарта NVIDIA GeForce RTX 3080 Ti 12GB GDDR6X', 78000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-008', id, 'NVIDIA RTX 3090', 'nvidia-rtx-3090', 'Видеокарта NVIDIA GeForce RTX 3090 24GB GDDR6X', 115000.00, 1, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-009', id, 'NVIDIA RTX 3090 Ti', 'nvidia-rtx-3090-ti', 'Видеокарта NVIDIA GeForce RTX 3090 Ti 24GB GDDR6X', 145000.00, 1, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

-- RTX 40 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-010', id, 'NVIDIA RTX 4060', 'nvidia-rtx-4060', 'Видеокарта NVIDIA GeForce RTX 4060 8GB GDDR6', 29500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-011', id, 'NVIDIA RTX 4060 Ti 8GB', 'nvidia-rtx-4060-ti-8gb', 'Видеокарта NVIDIA GeForce RTX 4060 Ti 8GB GDDR6', 37500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-012', id, 'NVIDIA RTX 4060 Ti 16GB', 'nvidia-rtx-4060-ti-16gb', 'Видеокарта NVIDIA GeForce RTX 4060 Ti 16GB GDDR6', 44500.00, 6, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-013', id, 'NVIDIA RTX 4070', 'nvidia-rtx-4070', 'Видеокарта NVIDIA GeForce RTX 4070 12GB GDDR6X', 56000.00, 7, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-014', id, 'NVIDIA RTX 4070 SUPER', 'nvidia-rtx-4070-super', 'Видеокарта NVIDIA GeForce RTX 4070 SUPER 12GB GDDR6X', 62500.00, 5, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-015', id, 'NVIDIA RTX 4070 Ti', 'nvidia-rtx-4070-ti', 'Видеокарта NVIDIA GeForce RTX 4070 Ti 12GB GDDR6X', 73000.00, 4, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-016', id, 'NVIDIA RTX 4070 Ti SUPER', 'nvidia-rtx-4070-ti-super', 'Видеокарта NVIDIA GeForce RTX 4070 Ti SUPER 16GB GDDR6X', 82500.00, 3, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-017', id, 'NVIDIA RTX 4080', 'nvidia-rtx-4080', 'Видеокарта NVIDIA GeForce RTX 4080 16GB GDDR6X', 105000.00, 2, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-018', id, 'NVIDIA RTX 4080 SUPER', 'nvidia-rtx-4080-super', 'Видеокарта NVIDIA GeForce RTX 4080 SUPER 16GB GDDR6X', 115000.00, 2, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'videocards';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-019', id, 'NVIDIA RTX 4090', 'nvidia-rtx-4090', 'Видеокарта NVIDIA GeForce RTX 4090 24GB GDDR6X', 165000.00, 1, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';