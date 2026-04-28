--liquibase formatted sql

--changeset tyzhprogramist:20
INSERT INTO users (username, email, password, first_name, last_name, role, date_joined, notifications, is_active, email_verified)
VALUES ('admin', 'admin@example.com', '$2a$10$oa/jJA9r94ql5oDtATc4PO/kgaUb5CucwgXNX1d2mNqE0ML8FIqVC', 'Admin', 'User', 'ADMIN', NOW(), TRUE, TRUE, FALSE);

-- Insert default moderator user
INSERT INTO users (username, email, password, first_name, last_name, role, date_joined, notifications, is_active, email_verified)
VALUES ('moderator', 'moderator@example.com', '$2a$10$TFtOhXB6m0.jZHC.gXDAauEIjjkIN1ln9.EtXYiroxo71f91oEakC', 'Moderator', 'User', 'MODERATOR', NOW(), TRUE, TRUE, FALSE);

-- Insert default site settings
INSERT INTO site_settings (pickup_address, pickup_phone, pickup_working_hours, delivery_cost, free_delivery_threshold)
VALUES ('г. Москва, ул. Примерная, д. 1', '+7 (999) 123-45-67', 'Пн-Пт 10:00-20:00, Сб-Вс 11:00-18:00', 500.00, 5000.00);