-- 19-banners.sql
-- Вставка начальных рекламных баннеров (PostgreSQL)

INSERT INTO banners (title, description, image_url, link, target_blank, display_order, is_active, created_at, updated_at) VALUES
('🔥 Черная пятница', 'Скидки до 50% на всю технику', '', '/catalog', FALSE, 1, TRUE, NOW(), NOW()),
('🖥️ Собери свой ПК', 'Конфигуратор ПК с проверкой совместимости', '', '/configurator', FALSE, 2, TRUE, NOW(), NOW()),
('🔧 Сервисный центр', 'Профессиональный ремонт техники', '', '/service', FALSE, 3, TRUE, NOW(), NOW()),
('💬 Помощь эксперта', 'Консультация по выбору комплектующих', '', '#', FALSE, 4, TRUE, NOW(), NOW()),
('🎮 Игровые ПК', 'Готовые решения для максимального FPS', '', '/catalog?category=gaming', FALSE, 5, TRUE, NOW(), NOW()),
('📦 Бесплатная доставка', 'При заказе от 5000 ₽', '', '/cart', FALSE, 6, TRUE, NOW(), NOW()),
('🔄 Рассрочка 0%', 'Покупайте сейчас, платите потом', '', '/catalog', FALSE, 7, TRUE, NOW(), NOW()),
('🎁 Подарочные карты', 'Отличный подарок для технаря', '', '/catalog', FALSE, 8, TRUE, NOW(), NOW());

-- Проверка что данные вставлены
DO $$
DECLARE
    banner_count INTEGER;
BEGIN
    SELECT COUNT(*) INTO banner_count FROM banners;
    RAISE NOTICE 'Inserted % banners', banner_count;
END $$;