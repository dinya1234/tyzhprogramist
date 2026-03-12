--liquibase formatted sql

--changeset tyzhprogramist:20-2
-- Insert default categories
INSERT INTO categories (name, slug, sort_order) VALUES
('Процессоры', 'processory', 1),
('Материнские платы', 'motherboards', 2),
('Видеокарты', 'videocards', 3),
('Оперативная память', 'ram', 4),
('Накопители', 'storage', 5),
('Блоки питания', 'power-supplies', 6),
('Корпуса', 'cases', 7),
('Охлаждение', 'cooling', 8);

-- Insert default component types
INSERT INTO component_types (name, order_step) VALUES
('Процессор', 1),
('Материнская плата', 2),
('Оперативная память', 3),
('Видеокарта', 4),
('Накопитель', 5),
('Блок питания', 6),
('Корпус', 7),
('Охлаждение', 8);