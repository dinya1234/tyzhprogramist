--liquibase formatted sql

--changeset tyzhprogramist:21-4
-- ==================== ВИДЕОКАРТЫ NVIDIA ====================
-- RTX 30 series
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-001', id, 'NVIDIA RTX 3050', 'nvidia-rtx-3050', 'Видеокарта NVIDIA GeForce RTX 3050 8GB GDDR6', 23000.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'videocards';

-- ... (все карты NVIDIA) ...
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'GPU-NVDA-019', id, 'NVIDIA RTX 4090', 'nvidia-rtx-4090', 'Видеокарта NVIDIA GeForce RTX 4090 24GB GDDR6X', 165000.00, 1, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'videocards';