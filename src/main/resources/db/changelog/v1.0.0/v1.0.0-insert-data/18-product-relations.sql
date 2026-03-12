--liquibase formatted sql

--changeset tyzhprogramist:22
-- ==================== СВЯЗИ ТОВАРОВ ====================
-- Связи для новых товаров
INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-003' AND p2.sku = 'GPU-NVDA-002';

-- ... (все остальные связи) ...
INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'GPU-NVDA-019' AND p2.sku = 'PSU-008';