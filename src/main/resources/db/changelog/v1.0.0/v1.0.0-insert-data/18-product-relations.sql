--liquibase formatted sql

--changeset tyzhprogramist:22
-- ==================== СВЯЗИ ТОВАРОВ ====================
-- Связи для новых товаров
INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-003' AND p2.sku = 'GPU-NVDA-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-003' AND p2.sku = 'RAM-DDR4-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-003' AND p2.sku = 'SSD-NVME-002';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-AMD-004' AND p2.sku = 'GPU-AMD-003';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-AMD-004' AND p2.sku = 'MB-001';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-AMD-010' AND p2.sku = 'GPU-NVDA-015';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-INTEL-011' AND p2.sku = 'GPU-NVDA-019';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'GPU-NVDA-002' AND p2.sku = 'PSU-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'GPU-NVDA-019' AND p2.sku = 'PSU-008';

-- Связи для старых товаров (CPU-001, GPU-001 и т.д.)
INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'GPU-001';

INSERT INTO related_products (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-002' AND p2.sku = 'GPU-002';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'RAM-001';

INSERT INTO frequently_bought_together (product_id, related_product_id)
SELECT p1.id, p2.id
FROM products p1, products p2
WHERE p1.sku = 'CPU-001' AND p2.sku = 'SSD-001';