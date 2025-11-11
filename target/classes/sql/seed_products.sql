-- Seed data for customer product search module.
-- Run these statements in MySQL Workbench (adjust schema name if different).

-- 1) Add optional columns (run once; skip if already present).
ALTER TABLE mydb.product
    ADD COLUMN IF NOT EXISTS description TEXT NULL;
ALTER TABLE mydb.product
    ADD COLUMN IF NOT EXISTS image_url VARCHAR(500) NULL;

-- 2) Update existing products with richer content and local images (copy images to
--    src/main/webapp/resources/images/products before running the updates).
UPDATE mydb.product SET
    stock_quantity = 15,
    description = 'iPhone 15 Pro Max với chip A17 Pro, camera tele 5x và khung titan.',
    image_url = '/resources/images/products/iphone15promax.jpg'
WHERE id = 1;

UPDATE mydb.product SET
    stock_quantity = 20,
    description = 'iPhone 14 128GB với màn hình Super Retina XDR 6.1 inch.',
    image_url = '/resources/images/products/iphone14.jpg'
WHERE id = 2;

UPDATE mydb.product SET
    stock_quantity = 12,
    description = 'Samsung Galaxy S24 Ultra trang bị camera 200MP và bút S Pen.',
    image_url = '/resources/images/products/samsungs24ultra.jpg'
WHERE id = 3;

UPDATE mydb.product SET
    stock_quantity = 30,
    description = 'Samsung Galaxy A54 5G màn hình 120Hz, pin 5000mAh.',
    image_url = '/resources/images/products/samsunga54.jpg'
WHERE id = 4;

UPDATE mydb.product SET
    stock_quantity = 18,
    description = 'Xiaomi 14T Pro dùng chip Dimensity 9200+, sạc nhanh 120W.',
    image_url = '/resources/images/products/xiaomi14tpro.jpg'
WHERE id = 5;

UPDATE mydb.product SET
    stock_quantity = 10,
    description = 'MacBook Air M2 13 inch với chip Apple M2, 8GB RAM.',
    image_url = '/resources/images/products/macbookairm2.jpg'
WHERE id = 6;

UPDATE mydb.product SET
    stock_quantity = 8,
    description = 'Dell XPS 13 9340 thiết kế viền siêu mỏng, CPU Intel Core Gen 13.',
    image_url = '/resources/images/products/dellxps13.jpg'
WHERE id = 7;

UPDATE mydb.product SET
    stock_quantity = 25,
    description = 'Tai nghe AirPods Pro 2 chống ồn chủ động, sạc USB-C.',
    image_url = '/resources/images/products/airpodspro2.jpg'
WHERE id = 8;

UPDATE mydb.product SET
    stock_quantity = 40,
    description = 'Sạc nhanh Anker 65W hỗ trợ ba cổng PD cho cả laptop.',
    image_url = '/resources/images/products/sacanker65w.jpg'
WHERE id = 9;

UPDATE mydb.product SET
    stock_quantity = 35,
    description = 'Ốp lưng Spigen MagSafe cho iPhone 15 Pro, chất liệu TPU.',
    image_url = '/resources/images/products/oplungiphone15pro.jpg'
WHERE id = 10;

-- 3) Insert additional showcase products (optional).
INSERT INTO mydb.product
    (name, brand, warranty_until, sale_price, purchase_price, stock_quantity,
     manufacture_date, expiry_date, description, image_url)
VALUES
    ('iPad Air 5 Wi-Fi 64GB', 'Apple', '2026-03-01', 15490000, 12900000, 22,
     '2025-02-10', NULL,
     'iPad Air 5 màn 10.9 inch, chip M1 mạnh mẽ cho học tập và giải trí.',
     '/resources/images/products/ipadair5.jpg'),
    ('Apple Watch Series 9 GPS 45mm', 'Apple', '2026-05-15', 11990000, 9500000, 18,
     '2025-02-20', NULL,
     'Apple Watch S9 với cảm biến sức khỏe đầy đủ, hỗ trợ Double Tap.',
     '/resources/images/products/applewatchs9.jpg'),
    ('Samsung Galaxy Tab S9 FE', 'Samsung', '2026-04-30', 12990000, 10200000, 16,
     '2025-03-05', NULL,
     'Galaxy Tab S9 FE màn 10.9 inch, kèm bút S Pen, pin 8000mAh.',
     '/resources/images/products/galaxytabs9fe.jpg'),
    ('Loa Bluetooth JBL Charge 6', 'JBL', '2026-06-01', 4990000, 3600000, 28,
     '2025-02-25', NULL,
     'Loa JBL Charge 6 âm thanh mạnh mẽ, pin nghe 20 giờ, chuẩn IP67.',
     '/resources/images/products/jblcharge6.jpg');
