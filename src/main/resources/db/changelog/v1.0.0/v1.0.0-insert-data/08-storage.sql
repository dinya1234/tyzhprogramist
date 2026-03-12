--liquibase formatted sql

--changeset tyzhprogramist:21-6
-- ==================== НАКОПИТЕЛИ SSD ====================
-- SATA SSD
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-001', id, 'Samsung 870 EVO 250GB', 'samsung-870-evo-250gb', 'SSD накопитель Samsung 870 EVO 250GB SATA III', 3500.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-002', id, 'Samsung 870 EVO 500GB', 'samsung-870-evo-500gb', 'SSD накопитель Samsung 870 EVO 500GB SATA III', 5500.00, 30, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-003', id, 'Samsung 870 EVO 1TB', 'samsung-870-evo-1tb', 'SSD накопитель Samsung 870 EVO 1TB SATA III', 9500.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-004', id, 'Samsung 870 QVO 2TB', 'samsung-870-qvo-2tb', 'SSD накопитель Samsung 870 QVO 2TB SATA III', 14500.00, 12, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-005', id, 'Samsung 870 QVO 4TB', 'samsung-870-qvo-4tb', 'SSD накопитель Samsung 870 QVO 4TB SATA III', 26500.00, 8, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-006', id, 'Kingston A400 480GB', 'kingston-a400-480gb', 'SSD накопитель Kingston A400 480GB SATA III', 3500.00, 30, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-007', id, 'Kingston A400 960GB', 'kingston-a400-960gb', 'SSD накопитель Kingston A400 960GB SATA III', 6200.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-008', id, 'Crucial BX500 500GB', 'crucial-bx500-500gb', 'SSD накопитель Crucial BX500 500GB SATA III', 4000.00, 30, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-009', id, 'Crucial BX500 1TB', 'crucial-bx500-1tb', 'SSD накопитель Crucial BX500 1TB SATA III', 7200.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-SATA-010', id, 'WD Blue 500GB', 'wd-blue-500gb', 'SSD накопитель WD Blue 500GB SATA III', 5200.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

-- NVMe SSD
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-001', id, 'Samsung 980 500GB', 'samsung-980-500gb', 'SSD накопитель Samsung 980 500GB NVMe M.2', 5500.00, 25, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-002', id, 'Samsung 980 1TB', 'samsung-980-1tb', 'SSD накопитель Samsung 980 1TB NVMe M.2', 9000.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-003', id, 'Samsung 980 PRO 500GB', 'samsung-980-pro-500gb', 'SSD накопитель Samsung 980 PRO 500GB NVMe M.2 PCIe 4.0', 7200.00, 18, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-004', id, 'Samsung 980 PRO 1TB', 'samsung-980-pro-1tb', 'SSD накопитель Samsung 980 PRO 1TB NVMe M.2 PCIe 4.0', 11500.00, 15, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-005', id, 'Samsung 980 PRO 2TB', 'samsung-980-pro-2tb', 'SSD накопитель Samsung 980 PRO 2TB NVMe M.2 PCIe 4.0', 21500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-006', id, 'Samsung 990 PRO 1TB', 'samsung-990-pro-1tb', 'SSD накопитель Samsung 990 PRO 1TB NVMe M.2 PCIe 4.0', 13500.00, 12, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-007', id, 'Samsung 990 PRO 2TB', 'samsung-990-pro-2tb', 'SSD накопитель Samsung 990 PRO 2TB NVMe M.2 PCIe 4.0', 24500.00, 8, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-008', id, 'WD Black SN770 500GB', 'wd-black-sn770-500gb', 'SSD накопитель WD Black SN770 500GB NVMe M.2 PCIe 4.0', 5200.00, 20, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-009', id, 'WD Black SN770 1TB', 'wd-black-sn770-1tb', 'SSD накопитель WD Black SN770 1TB NVMe M.2 PCIe 4.0', 8500.00, 18, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-010', id, 'WD Black SN770 2TB', 'wd-black-sn770-2tb', 'SSD накопитель WD Black SN770 2TB NVMe M.2 PCIe 4.0', 15500.00, 12, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-011', id, 'Kingston KC3000 1TB', 'kingston-kc3000-1tb', 'SSD накопитель Kingston KC3000 1TB NVMe M.2 PCIe 4.0', 9500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-012', id, 'Kingston KC3000 2TB', 'kingston-kc3000-2tb', 'SSD накопитель Kingston KC3000 2TB NVMe M.2 PCIe 4.0', 17500.00, 10, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-013', id, 'Crucial P3 Plus 500GB', 'crucial-p3-plus-500gb', 'SSD накопитель Crucial P3 Plus 500GB NVMe M.2 PCIe 4.0', 4500.00, 22, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-014', id, 'Crucial P3 Plus 1TB', 'crucial-p3-plus-1tb', 'SSD накопитель Crucial P3 Plus 1TB NVMe M.2 PCIe 4.0', 7500.00, 20, NOW(), TRUE, TRUE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'SSD-NVME-015', id, 'Crucial P3 Plus 2TB', 'crucial-p3-plus-2tb', 'SSD накопитель Crucial P3 Plus 2TB NVMe M.2 PCIe 4.0', 13500.00, 15, NOW(), TRUE, TRUE, FALSE
FROM categories WHERE slug = 'storage';

-- HDD
INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-001', id, 'Seagate BarraCuda 1TB', 'seagate-barracuda-1tb', 'Жесткий диск Seagate BarraCuda 1TB 7200rpm', 4200.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-002', id, 'Seagate BarraCuda 2TB', 'seagate-barracuda-2tb', 'Жесткий диск Seagate BarraCuda 2TB 7200rpm', 5600.00, 20, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-003', id, 'Seagate BarraCuda 4TB', 'seagate-barracuda-4tb', 'Жесткий диск Seagate BarraCuda 4TB 5400rpm', 9500.00, 15, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-004', id, 'WD Blue 1TB', 'wd-blue-1tb', 'Жесткий диск WD Blue 1TB 7200rpm', 4200.00, 25, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-005', id, 'WD Blue 2TB', 'wd-blue-2tb', 'Жесткий диск WD Blue 2TB 7200rpm', 5800.00, 20, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-006', id, 'WD Black 1TB', 'wd-black-1tb', 'Жесткий диск WD Black 1TB 7200rpm', 6200.00, 15, NOW(), TRUE, FALSE, TRUE
FROM categories WHERE slug = 'storage';

INSERT INTO products (sku, category_id, name, slug, short_description, price, quantity, created_at, is_active, is_new, is_bestseller)
SELECT 'HDD-007', id, 'Toshiba P300 2TB', 'toshiba-p300-2tb', 'Жесткий диск Toshiba P300 2TB 7200rpm', 5400.00, 18, NOW(), TRUE, FALSE, FALSE
FROM categories WHERE slug = 'storage';