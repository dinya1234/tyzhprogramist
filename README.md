# 🛒 Интернет-магазин «Тыжпрограммист»

> Онлайн-платформа для продажи цифровой и компьютерной техники с уникальными сервисами: онлайн-консультант, конфигуратор ПК, сервисный центр и система сравнения товаров и сборок.

---

## 📌 О проекте

«Тыжпрограммист» — это современный интернет-магазин, ориентированный на энтузиастов, геймеров и профессионалов.  
Проект сочетает в себе классическую e-commerce функциональность и уникальные инструменты для самостоятельной сборки ПК, сравнения комплектующих и экспертной поддержки.

---

## 🧩 Структура сайта

### Главная страница
- Навигация по основным категориям
- Баннеры с акциями, новинками, топовыми товарами
- Быстрый доступ к сервисам (сборка ПК, ремонт)

### Каталог товаров
- Древовидная структура категорий
- Карточки товаров (фото, цена, рейтинг, наличие)
- Расширенные карточки (характеристики, отзывы, Q&A, рекомендации)

### Конфигуратор ПК
- Пошаговый выбор комплектующих
- Автоматическая проверка совместимости (сокет, форм-фактор, энергопотребление)
- Калькулятор мощности БП
- Публикация сборки в каталог, шеринг в соцсетях
- Сохранение в профиле, формирование сметы

### Сервис и ремонт
- Форма заявки на ремонт/диагностику
- Выбор техники, описание проблемы, загрузка фото/видео
- Статусы ремонта, гарантии, стоимость
- Отслеживание в личном кабинете

### Корзина покупок
- Управление товарами (количество, удаление)
- Выбор доставки (курьер, самовывоз, постаматы) и оплаты

### Личный кабинет
- История заказов (товары + заявки на ремонт)
- Сохранённые товары, сравнения, черновики сборок
- Настройки уведомлений

### Онлайн-консультант
- Гибридная система (чат-бот + живой специалист)

---

## 📦 Категории товаров

| Категория | Примеры |
|-----------|---------|
| Компьютерная техника и комплектующие | Ноутбуки, видеокарты, процессоры, ОЗУ, SSD, БП, корпуса |
| Периферия и аксессуары | Клавиатуры, мыши, наушники, колонки, сетевое оборудование |
| Игровая зона | Консоли, видеоигры, игровые кресла, рули, джойстики |
| Программное обеспечение | ОС, офисные пакеты, антивирусы, специализированный софт |

---

## ⚙️ Детальный функционал

### Для покупателей (Frontend)

**Умный каталог с фильтрацией**
- По цене, брендам, характеристикам (объём памяти, диагональ и т.д.)
- По наличию в конкретном магазине, рейтингу
- Сортировка: популярность, цена, новизна, рейтинг

**Система сравнения**
- До 4 товаров в таблице
- ✅ Уникально: сравнение готовых и кастомных сборок ПК

**Интерактивные сервисы**
- Конфигуратор ПК + проверка совместимости
- Онлайн-консультант (бот + человек)

**Пользовательский контент**
- Отзывы с фото/видео
- Q&A на карточках товаров

**Работа с заказами**
- Доставка: курьер (в день заказа), самовывоз, постаматы
- Оплата: онлайн, при получении, кредит/рассрочка (0-0-10)

**Маркетинг**
- Топ продаж, выбор редакции, новинки, товары со скидкой
- Акции, промокоды

### Для администраторов (Backend / Админ-панель)

- **Управление товарами**: CRUD категорий, товаров, характеристик
- **Управление заказами**: статусы, доставка, возвраты
- **Управление сервисом**: заявки на ремонт, статусы, стоимость
- **Модерация**: отзывы, вопросы, пользовательские сборки
- **Аналитика**: продажи, популярные товары/сборки, эффективность акций
- **Управление пользователями**: список, заказы, активность

---

## 🌟 Ключевые уникальные особенности

| Особенность | Описание |
|-------------|----------|
| 🧠 Умный конфигуратор ПК | Глубокая проверка совместимости + интеллектуальный помощник |
| 👥 Сообщество сборок | Публикация и сравнение кастомных сборок |
| 💬 Гибридный онлайн-консультант | Чат-бот + живой эксперт |
| 🔍 Сравнение сборок и товаров | Продвинутая система сравнения |

---

## 💬 Система онлайн-консультанта

### Цель
Мгновенное подключение к живому эксперту для персональной помощи и повышения доверия.

### Триггеры вызова
- Фиксированная кнопка «Помощь в выборе? Спросите эксперта»
- «Быстрый вопрос по товару» — с артикулом
- «Нужна помощь со сборкой?» — внутри конфигуратора
- Страница «Консультация» в меню

### Как работает
1. Пользователь нажимает кнопку → открывается чат
2. Автосообщение: *«Соединяем с консультантом... Среднее время ответа — 2 минуты»*
3. Пользователь задаёт вопрос в свободной форме (можно скриншот/ссылку)
4. Консультант (человек) видит:
   - Страницу пользователя
   - Товар (если с карточки)
   - Корзину и черновик сборки (при согласии)
5. Инструменты консультанта:
   - Ссылки на товары/сборки
   - Просмотр наличия
   - Отправка PDF-сметы на email
   - Шаблоны ответов

### Если все заняты
- Сообщение с номером очереди
- Кнопка «Получить консультацию по телефону» (обратный звонок)

### Завершение
- Ручное или автоматическое (30 мин неактивности)
- Email с транскрипцией диалога и ссылками

### Аналитика для администратора
- Количество диалогов
- Среднее время ответа
- Конверсия в заказы
- Рейтинг консультантов

> 💡 **Итог**: Онлайн-консультант — это цифровая витрина экспертизы, имитирующая живое общение в офлайн-магазине.
---


# 🏗️ Архитектура и структура проекта

Проект «Тыжпрограммист» построен на **микросервисной монолитной** архитектуре с чётким разделением на бэкенд (Java Spring Boot) и фронтенд (React). Взаимодействие между клиентом и сервером происходит через REST API и WebSocket-соединения (для онлайн-консультанта).



---

## 🧩 Используемый стек технологий

### Backend (Java)
| Компонент | Технология | Назначение |
|-----------|------------|-------------|
| **Framework** | Spring Boot 3.x | Основной каркас приложения |
| **Web & REST** | Spring Web | REST API контроллеры |
| **Security** | Spring Security + JWT | Аутентификация, авторизация |
| **Database** | Spring Data JPA (Hibernate) | ORM, работа с БД |
| **Validation** | Spring Validation | Валидация входных данных |
| **WebSocket** | Spring WebSocket + STOMP | Онлайн-чат консультанта |
| **Миграции** | Liquibase | Управление схемой БД |
| **Сборка** | Maven | Управление зависимостями |

### Дополнительные библиотеки
```xml
<!-- JWT -->
jjwt-api, jjwt-impl, jjwt-jackson (0.12.5)

<!-- Утилиты -->
Lombok (сокращение кода)
MapStruct (маппинг DTO ↔ Entity)

<!-- База данных -->
PostgreSQL Driver
```


# 🧩 Frontend (React)

[![React](https://img.shields.io/badge/React-18-61DAFB?logo=react)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-4.x-646CFF?logo=vite)](https://vitejs.dev/)
[![Docker](https://img.shields.io/badge/Docker-✓-2496ED?logo=docker)](https://www.docker.com/)

## 🚀 Технологический стек

### ⚛️ Frontend

| Компонент           | Технология                                      |
|---------------------|-------------------------------------------------|
| **Framework**       | React 18+ ![React](https://img.shields.io/badge/-React-61DAFB?logo=react&logoColor=black) |
| **Сборка**          | Vite ![Vite](https://img.shields.io/badge/-Vite-646CFF?logo=vite&logoColor=white) |
| **Маршрутизация**   | React Router ![React Router](https://img.shields.io/badge/-React%20Router-CA4245?logo=reactrouter&logoColor=white) |
| **Управление состоянием** | Context API (Auth, Cart, Chat) |
| **HTTP клиент**     | Axios ![Axios](https://img.shields.io/badge/-Axios-5A29E4?logo=axios&logoColor=white) |
| **WebSocket**       | STOMP.js / SockJS |
| **Стили**           | CSS Modules ![CSS Modules](https://img.shields.io/badge/-CSS%20Modules-1572B6?logo=css3&logoColor=white) |

---

## 🛠️ Инфраструктура

| Компонент           | Решение                                             |
|---------------------|-----------------------------------------------------|
| **Контейнеризация** | Docker + Docker Compose ![Docker](https://img.shields.io/badge/-Docker-2496ED?logo=docker&logoColor=white) |
| **База данных**     | PostgreSQL ![PostgreSQL](https://img.shields.io/badge/-PostgreSQL-4169E1?logo=postgresql&logoColor=white) |
| **Миграции**        | Liquibase ![Liquibase](https://img.shields.io/badge/-Liquibase-2962FF?logo=liquibase&logoColor=white) |
| **Версионирование** | Git (GitHub) ![Git](https://img.shields.io/badge/-Git-F05032?logo=git&logoColor=white) |

---


## Структура проекта(Backend)

```bash
src/main/java/ru/shop/tyzhprogramist/
│
├── config/                 # ⚙️ Конфигурации Spring
│   ├── SecurityConfig.java     # JWT, эндпоинты доступа
│   ├── WebSocketConfig.java    # STOMP, брокер сообщений
│   └── ...
│
├── controller/             # 🌐 REST и WebSocket контроллеры
│   ├── AuthController.java
│   ├── ProductController.java
│   ├── CartController.java
│   ├── PcBuildController.java
│   ├── RepairRequestController.java
│   ├── ChatController.java          # REST-часть чата (история)
│   └── ChatWebSocketController.java # Обработка сообщений в реальном времени
│
├── dto/                    # 📦 Data Transfer Objects
│   ├── request/                 # Входящие DTO (Login, CreateBuild...)
│   └── response/                # Исходящие DTO (AuthResponse, ProductResponse...)
│
├── entity/                 # 🗄️ JPA сущности (PostgreSQL)
│   ├── User.java
│   ├── Product.java / ProductItem.java
│   ├── Category.java
│   ├── Cart.java / CartItem.java
│   ├── Order.java / OrderItem.java
│   ├── PcBuild.java / PcBuildComponent.java
│   ├── ChatSession.java / ChatMessage.java
│   ├── RepairRequest.java
│   ├── EntityRelation.java       # Связи и совместимость компонентов
│   ├── RefreshToken.java
│   ├── SiteSettings.java
│   └── enums/                    # ChatStatus, OrderStatus, SenderType...
│
├── repository/             # 🧠 Spring Data JPA (15+ интерфейсов)
├── service/                # 📐 Бизнес-логика
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── CompatibilityService.java   # Алгоритм проверки совместимости
│   ├── ChatService.java
│   ├── PcBuildService.java
│   └── ...
│
├── security/               # 🔒 JWT-слой
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
│
├── exception/              # ❌ Глобальная обработка ошибок
│   ├── GlobalExceptionHandler.java
│   ├── NotFoundException.java
│   └── BadRequestException.java
│
└── ...
```


## Структура фронтенда (React)
```bash
tzyhprogramist-frontend/
├── public/                         # Статика
├── src/
│   ├── components/                 # Переиспользуемые компоненты
│   │   ├── Chat/                   # Виджет онлайн-консультанта
│   │   │   ├── ChatWidget.jsx
│   │   │   ├── ChatWindow.jsx
│   │   │   ├── ChatMessage.jsx
│   │   │   └── Chat.css
│   │   ├── Header.jsx
│   │   ├── Footer.jsx
│   │   └── ProductCard.jsx
│   ├── pages/                      # Страницы приложения
│   │   ├── admin/                  # Админ-панель
│   │   │   ├── AdminPanel.jsx
│   │   │   └── ConsultantPanel.jsx (панель консультанта)
│   │   ├── HomePage.jsx            # Главная
│   │   ├── CatalogPage.jsx         # Каталог с фильтрами
│   │   ├── ProductPage.jsx         # Карточка товара
│   │   ├── ConfiguratorPage.jsx    # Конфигуратор ПК
│   │   ├── ComparisonPage.jsx      # Сравнение товаров/сборок
│   │   ├── CartPage.jsx            # Корзина
│   │   ├── CheckoutPage.jsx        # Оформление заказа
│   │   ├── OrderSuccessPage.jsx    # Успешный заказ
│   │   ├── ProfilePage.jsx         # Личный кабинет
│   │   ├── ServicePage.jsx         # Сервис/ремонт
│   │   ├── ChatHistory.jsx         # История чатов
│   │   └── LoginPage.jsx           # Вход/регистрация
│   ├── context/                    # React Context API
│   │   ├── AuthContext.jsx         # Авторизация
│   │   ├── CartContext.jsx         # Корзина
│   │   └── ChatContext.jsx         # Чат (WebSocket)
│   ├── services/                   # API клиенты
│   │   └── api.js                  # Axios + WebSocket
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── package.json
├── vite.config.js
└── .gitignore
```


## Управление базой данных (Liquibase)
```bash
db.changelog/
├── master.xml                      # Главный файл changelog
├── v1.0.0/
│   ├── v1.0.0-create-tables.sql    # Создание всех таблиц
│   ├── v1.0.0-insert-data.sql      # Наполнение данными
│   ├── 01-users-settings.sql
│   ├── 02-categories-types.sql
│   ├── 03-processors.sql           # Процессоры (Intel/AMD)
│   ├── 04-motherboards.sql         # Материнские платы
│   ├── 05-ram.sql                  # Оперативная память
│   ├── 06-graphics-cards-nvidia.sql
│   ├── 07-graphics-cards-amd.sql
│   ├── 08-storage.sql              # SSD/HDD
│   ├── 09-power-supplies.sql       # Блоки питания
│   ├── 10-cases.sql                # Корпуса
│   ├── 11-cooling.sql              # Системы охлаждения
│   ├── 12-peripherals-keyboards-mice.sql
│   ├── 13-peripherals-audio-video.sql
│   ├── 14-peripherals-networking.sql
│   ├── 15-prebuilt-pcs.sql         # Готовые ПК
│   ├── 16-laptops-work.sql         # Рабочие ноутбуки
│   ├── 17-laptops-gaming.sql       # Игровые ноутбуки
│   └── 18-product-relations.sql    # Связи товаров (совместимость)
```
---
## Модель данных (основные связи)
```bash
User 1───* Order
User 1───* Cart
User 1───* PcBuild
User 1───* ChatSession
User 1───* RepairRequest
User 1───* ProductFeedback

Product 1───* ProductItem (склад)
Product *───* Category (M2M)
Product *───* Product (совместимость через EntityRelation)

PcBuild *───* Product (комплектующие, M2M через PcBuildComponent)

ChatSession 1───* ChatMessage
```
## API endpoints есть в http://localhost:8080/swagger-ui/index.html (сработает если запустите проект)
## 👤 User Controller (`/api/users`)

Управление пользователями (доступно администраторам и владельцам профиля).

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| `GET` | `/api/users` | Получить список всех пользователей (пагинация) | ADMIN |
| `GET` | `/api/users/{id}` | Получить пользователя по ID | ADMIN / OWNER |
| `GET` | `/api/users/me` | Получить профиль текущего пользователя | USER |
| `GET` | `/api/users/search` | Поиск пользователей по имени/email | ADMIN |
| `GET` | `/api/users/statistics` | Статистика по пользователям (всего, новых и т.д.) | ADMIN |
| `PUT` | `/api/users/{id}/role` | Изменить роль пользователя | ADMIN |
| `PUT` | `/api/users/{id}/active` | Заблокировать/разблокировать пользователя | ADMIN |
| `PUT` | `/api/users/me/password` | Сменить пароль текущего пользователя | USER |
| `DELETE` | `/api/users/{id}` | Удалить пользователя | ADMIN |

---

## ⚙️ Site Settings Controller (`/api/settings`)

Управление настройками магазина (доступно администраторам).

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| `GET` | `/api/settings` | Получить все настройки | ADMIN |
| `GET` | `/api/settings/public` | Получить публичные настройки (без ключей API) | ALL |
| `GET` | `/api/settings/working-hours` | Часы работы | ALL |
| `GET` | `/api/settings/pickup-phone` | Телефон пункта самовывоза | ALL |
| `GET` | `/api/settings/pickup-address` | Адрес пункта самовывоза | ALL |
| `GET` | `/api/settings/free-delivery-threshold` | Порог бесплатной доставки | ALL |
| `GET` | `/api/settings/delivery-cost` | Стоимость доставки | ALL |
| `GET` | `/api/settings/delivery-info` | Информация о доставке | ALL |
| `GET` | `/api/settings/contact-info` | Контактная информация | ALL |
| `GET` | `/api/settings/is-free-delivery` | Проверить, включена ли бесплатная доставка | ALL |
| `GET` | `/api/settings/is-configured` | Проверить, настроен ли магазин | ALL |
| `GET` | `/api/settings/calculate-delivery` | Рассчитать стоимость доставки (параметры: `?amount=...`) | ALL |
| `GET` | `/api/settings/statistics` | Статистика настроек | ADMIN |
| `PUT` | `/api/settings` | Обновить все настройки | ADMIN |
| `PUT` | `/api/settings/working-hours` | Обновить часы работы | ADMIN |
| `PUT` | `/api/settings/pickup-phone` | Обновить телефон | ADMIN |
| `PUT` | `/api/settings/pickup-address` | Обновить адрес | ADMIN |
| `PUT` | `/api/settings/free-delivery-threshold` | Обновить порог бесплатной доставки | ADMIN |
| `PUT` | `/api/settings/delivery-cost` | Обновить стоимость доставки | ADMIN |
| `POST` | `/api/settings/reset` | Сбросить настройки по умолчанию | ADMIN |
| `POST` | `/api/settings/initialize` | Инициализировать настройки (при первом запуске) | ADMIN |
| `DELETE` | `/api/settings/duplicates` | Удалить дублирующиеся настройки | ADMIN |

---

## 🔧 Repair Request Controller (`/api/repair-requests`)

Управление заявками на ремонт.

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| `GET` | `/api/repair-requests` | Список всех заявок (пагинация) | ADMIN |
| `GET` | `/api/repair-requests/{id}` | Получить заявку по ID | ADMIN / OWNER |
| `GET` | `/api/repair-requests/me` | Список заявок текущего пользователя | USER |
| `GET` | `/api/repair-requests/me/{id}` | Получить свою заявку по ID | USER |
| `GET` | `/api/repair-requests/status/{status}` | Фильтр заявок по статусу | ADMIN |
| `GET` | `/api/repair-requests/statistics` | Статистика по ремонтам | ADMIN |
| `POST` | `/api/repair-requests` | Создать новую заявку | USER |
| `POST` | `/api/repair-requests/me/{id}/cancel` | Отменить свою заявку | USER |
| `PUT` | `/api/repair-requests/{id}/status` | Изменить статус заявки | ADMIN |
| `PUT` | `/api/repair-requests/{id}/repair` | Обновить информацию о ремонте | ADMIN |
| `PUT` | `/api/repair-requests/{id}/issue` | Обновить описание проблемы | ADMIN |
| `PUT` | `/api/repair-requests/{id}/estimated-price` | Установить примерную стоимость | ADMIN |
| `PUT` | `/api/repair-requests/{id}/diagnostics` | Добавить результаты диагностики | ADMIN |
| `PUT` | `/api/repair-requests/{id}/complete` | Завершить ремонт | ADMIN |
| `PUT` | `/api/repair-requests/{id}/comment` | Добавить комментарий к заявке | ADMIN |

**Статусы ремонта**: `NEW`, `DIAGNOSTICS`, `WAITING_PARTS`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`

---

## 🔗 Entity Relation Controller (`/api/relations`)

Управление связями между сущностями (совместимость, сравнения).

### Совместимость (Compatibility)

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/relations/compatibility/{contentType}/{objectId}/compatible` | Совместимые объекты |
| `GET` | `/api/relations/compatibility/{contentType}/{objectId}/incompatible` | Несовместимые объекты |
| `GET` | `/api/relations/compatibility/products/{productId1}/{productId2}` | Проверить совместимость двух товаров |
| `GET` | `/api/relations/compatibility/product/{productId}/component-type/{componentTypeId}` | Получить совместимые компоненты для товара |
| `GET` | `/api/relations/compatibility/check` | Проверить совместимость набора компонентов (`?productIds=1,2,3`) |
| `GET` | `/api/relations/compatibility/rules/{contentType}/{objectId}` | Список правил совместимости |
| `GET` | `/api/relations/compatibility/statistics/{contentType}/{objectId}` | Статистика совместимости |
| `PUT` | `/api/relations/compatibility/{ruleId}` | Обновить правило совместимости |
| `POST` | `/api/relations/compatibility/products` | Добавить связь совместимости между товарами |
| `POST` | `/api/relations/compatibility/products/symmetric` | Добавить симметричную связь (A↔B) |
| `POST` | `/api/relations/compatibility/bulk` | Массовое добавление правил |
| `POST` | `/api/relations/compatibility/copy` | Копировать правила с одного объекта на другой |
| `DELETE` | `/api/relations/compatibility/{contentType}/{objectId}` | Удалить все правила для объекта |
| `DELETE` | `/api/relations/compatibility/products` | Удалить связь между товарами |

### Сравнения (Comparisons)

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/relations/comparisons` | Список всех сравнений текущего пользователя |
| `GET` | `/api/relations/comparisons/{comparisonName}` | Получить содержимое сравнения |
| `GET` | `/api/relations/comparisons/{comparisonName}/grouped` | Получить сравнение, сгруппированное по типам |
| `GET` | `/api/relations/comparisons/{comparisonName}/count` | Количество объектов в сравнении |
| `GET` | `/api/relations/comparisons/exists` | Проверить существование сравнения (`?name=...`) |
| `POST` | `/api/relations/comparisons/{comparisonName}/products/{productId}` | Добавить товар в сравнение |
| `POST` | `/api/relations/comparisons/{comparisonName}/pc-builds/{pcBuildId}` | Добавить сборку ПК в сравнение |
| `DELETE` | `/api/relations/comparisons/{comparisonName}` | Удалить всё сравнение |
| `DELETE` | `/api/relations/comparisons/{comparisonName}/products/{productId}` | Удалить товар из сравнения |
| `DELETE` | `/api/relations/comparisons/{comparisonName}/pc-builds/{pcBuildId}` | Удалить сборку из сравнения |

### Статистика и администрирование

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/relations/statistics` | Общая статистика по связям |
| `GET` | `/api/relations/most-compared-products` | Самые сравниваемые товары |
| `GET` | `/api/relations/admin/compatibility` | Все правила совместимости (админ) |
| `GET` | `/api/relations/admin/comparisons` | Все сравнения всех пользователей (админ) |
| `DELETE` | `/api/relations/admin/old-comparisons` | Удалить старые сравнения (админ) |

**Типы контента**: `PRODUCT`, `PC_BUILD`, `COMPONENT`

---

## 👤 Profile Controller (`/api/profile`)

Управление профилем текущего пользователя.

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/profile/me` | Получить профиль пользователя |
| `PUT` | `/api/profile/me/password` | Сменить пароль |
| `PUT` | `/api/profile/me/notifications` | Настроить уведомления (`email_enabled`, `push_enabled`) |
| `PUT` | `/api/profile/me/chat-consent` | Включить/выключить согласие на просмотр корзины консультантом |

---

## 📦 Product Controller (`/api/products`)

Управление товарами.

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| `GET` | `/api/products` | Список товаров (фильтры, пагинация) | ALL |
| `GET` | `/api/products/{id}` | Получить товар по ID | ALL |
| `GET` | `/api/products/slug/{slug}` | Получить товар по slug | ALL |
| `GET` | `/api/products/search` | Поиск товаров (`?q=...`) | ALL |
| `GET` | `/api/products/new` | Новинки (последние 10) | ALL |
| `GET` | `/api/products/bestsellers` | Топ продаж | ALL |
| `GET` | `/api/products/category/{categoryId}` | Товары по категории | ALL |
| `POST` | `/api/products` | Создать товар | ADMIN |
| `PUT` | `/api/products/{id}` | Обновить товар | ADMIN |
| `DELETE` | `/api/products/{id}` | Удалить товар | ADMIN |

---

## 🖥️ PC Build Controller (`/api/pc-builds`)

Управление сборками ПК.

### Личные сборки (авторизованный пользователь)

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/pc-builds/me` | Список моих сборок |
| `GET` | `/api/pc-builds/me/{id}` | Получить мою сборку по ID |
| `GET` | `/api/pc-builds/me/{id}/components` | Компоненты моей сборки |
| `GET` | `/api/pc-builds/me/{id}/total` | Итоговая цена сборки |
| `POST` | `/api/pc-builds/me` | Создать новую сборку |
| `POST` | `/api/pc-builds/me/{id}/components/{productId}` | Добавить компонент в сборку |
| `PUT` | `/api/pc-builds/me/{id}` | Обновить название/описание сборки |
| `DELETE` | `/api/pc-builds/me/{id}` | Удалить сборку |
| `DELETE` | `/api/pc-builds/me/{id}/components/{productId}` | Удалить компонент из сборки |

### Публичные сборки (все пользователи)

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/pc-builds/public` | Список опубликованных сборок |
| `GET` | `/api/pc-builds/public/{id}` | Публичная сборка по ID |
| `GET` | `/api/pc-builds/public/search` | Поиск по публичным сборкам |
| `GET` | `/api/pc-builds/public/recent` | Последние опубликованные сборки |

### Статистика и администрирование

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| `GET` | `/api/pc-builds` | Все сборки (админ) | ADMIN |
| `GET` | `/api/pc-builds/statistics` | Статистика по сборкам | ADMIN |

---

## 📦 Order Controller (`/api/orders`)

Управление заказами.

| Метод | Эндпоинт | Описание | Доступ |
|-------|----------|----------|--------|
| `GET` | `/api/orders` | Список всех заказов (пагинация) | ADMIN |
| `GET` | `/api/orders/{orderId}` | Получить заказ по ID | ADMIN / OWNER |
| `GET` | `/api/orders/me` | Мои заказы | USER |
| `GET` | `/api/orders/me/{orderId}` | Мой заказ по ID | USER |
| `GET` | `/api/orders/status/{status}` | Фильтр заказов по статусу | ADMIN |
| `GET` | `/api/orders/statistics` | Статистика по заказам | ADMIN |
| `POST` | `/api/orders` | Создать заказ | USER |
| `POST` | `/api/orders/me/{orderId}/cancel` | Отменить свой заказ | USER |
| `PUT` | `/api/orders/{orderId}/status` | Изменить статус заказа | ADMIN |

**Статусы заказа**: `PENDING`, `CONFIRMED`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`, `REFUNDED`

---

## 📎 File Attachment Controller (`/api/files`)

Управление файлами (изображения, видео, документы).

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/files/{contentType}/{objectId}` | Получить все файлы объекта |
| `GET` | `/api/files/{contentType}/{objectId}/images` | Изображения объекта |
| `GET` | `/api/files/{contentType}/{objectId}/videos` | Видео объекта |
| `GET` | `/api/files/{contentType}/{objectId}/documents` | Документы объекта |
| `GET` | `/api/files/{contentType}/{objectId}/main-image` | Главное изображение |
| `GET` | `/api/files/products/{productId}/images` | Изображения товара |
| `GET` | `/api/files/products/{productId}/main-image` | Главное изображение товара |
| `GET` | `/api/files/file/{fileId}` | Получить файл по ID |
| `GET` | `/api/files/search` | Поиск файлов |
| `GET` | `/api/files/recent` | Последние загруженные файлы |
| `GET` | `/api/files/statistics` | Статистика по файлам |
| `GET` | `/api/files/by-mime-type` | Фильтр по MIME-типу |
| `GET` | `/api/files/orphaned` | Найти файлы без владельца |
| `POST` | `/api/files/upload/{contentType}/{objectId}` | Загрузить несколько файлов |
| `POST` | `/api/files/upload/single/{contentType}/{objectId}` | Загрузить один файл |
| `POST` | `/api/files/products/{productId}/images` | Загрузить изображения для товара |
| `POST` | `/api/files/copy` | Копировать файлы между объектами |
| `PUT` | `/api/files/{fileId}/main` | Установить файл как главный |
| `PUT` | `/api/files/reorder` | Изменить порядок файлов |
| `DELETE` | `/api/files/{contentType}/{objectId}` | Удалить все файлы объекта |
| `DELETE` | `/api/files/{fileId}` | Удалить файл по ID |
| `DELETE` | `/api/files/orphaned` | Удалить файлы без владельца |

**Типы контента**: `PRODUCT`, `REPAIR_REQUEST`, `PC_BUILD`, `FEEDBACK`

---

## 🔧 Component Type Controller (`/api/component-types`)

Управление типами компонентов для конфигуратора ПК.

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/component-types` | Список всех типов компонентов |
| `GET` | `/api/component-types/{id}` | Получить тип по ID |
| `GET` | `/api/component-types/ordered` | Типы в порядке сборки |
| `GET` | `/api/component-types/first` | Первый шаг сборки |
| `GET` | `/api/component-types/last` | Последний шаг сборки |
| `GET` | `/api/component-types/next/{currentStep}` | Следующий шаг сборки |
| `GET` | `/api/component-types/step/{step}` | Тип по номеру шага |
| `GET` | `/api/component-types/total-steps` | Количество шагов сборки |
| `GET` | `/api/component-types/progress/{currentStep}` | Прогресс сборки |
| `GET` | `/api/component-types/order-range` | Диапазон шагов |
| `GET` | `/api/component-types/search` | Поиск по имени |
| `GET` | `/api/component-types/statistics` | Статистика по типам |
| `GET` | `/api/component-types/usage-statistics` | Статистика использования |
| `GET` | `/api/component-types/metadata` | Метаданные типов |
| `GET` | `/api/component-types/name/{name}` | Поиск по точному имени |
| `GET` | `/api/component-types/processors` | Типы процессоров |
| `GET` | `/api/component-types/memory` | Типы памяти |
| `GET` | `/api/component-types/storage` | Типы накопителей |
| `GET` | `/api/component-types/{id}/required` | Обязателен ли тип |
| `GET` | `/api/component-types/{id}/multiple-allowed` | Можно ли несколько |
| `GET` | `/api/component-types/{id}/is-processor` | Является ли процессором |
| `GET` | `/api/component-types/{id}/is-memory` | Является ли памятью |
| `GET` | `/api/component-types/{id}/is-storage` | Является ли накопителем |
| `GET` | `/api/component-types/{id}/compatible` | Совместимые типы |
| `GET` | `/api/component-types/{id}/incompatible` | Несовместимые типы |
| `GET` | `/api/component-types/{id}/compatibility-rules` | Правила совместимости |
| `GET` | `/api/component-types/compatibility/check` | Проверить совместимость типов |
| `POST` | `/api/component-types` | Создать новый тип | ADMIN |
| `POST` | `/api/component-types/reorder` | Изменить порядок шагов | ADMIN |
| `POST` | `/api/component-types/initialize-defaults` | Создать типы по умолчанию | ADMIN |
| `POST` | `/api/component-types/compatibility/rule` | Добавить правило совместимости | ADMIN |
| `POST` | `/api/component-types/compatibility/rule/symmetric` | Добавить симметричное правило | ADMIN |
| `PUT` | `/api/component-types/{id}` | Обновить тип | ADMIN |
| `DELETE` | `/api/component-types/{id}` | Удалить тип | ADMIN |

---

## 🗂️ Category Controller (`/api/categories`)

Управление категориями товаров.

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/categories` | Список всех категорий |
| `GET` | `/api/categories/tree` | Дерево категорий |
| `GET` | `/api/categories/roots` | Корневые категории |
| `GET` | `/api/categories/{slug}` | Получить категорию по slug |
| `GET` | `/api/categories/{id}/breadcrumbs` | Хлебные крошки для категории |
| `POST` | `/api/categories` | Создать категорию | ADMIN |
| `PUT` | `/api/categories/{id}` | Обновить категорию | ADMIN |
| `DELETE` | `/api/categories/{id}` | Удалить категорию | ADMIN |

---

## 🛒 Cart Controller (`/api/cart`)

Управление корзиной покупок.

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `GET` | `/api/cart` | Получить корзину |
| `POST` | `/api/cart/add` | Добавить товар (body: `{ productId, quantity }`) |
| `PUT` | `/api/cart/update` | Обновить количество (body: `{ productId, quantity }`) |
| `DELETE` | `/api/cart/remove/{productId}` | Удалить товар из корзины |
| `DELETE` | `/api/cart/clear` | Очистить корзину |

---

## 🔐 Auth Controller (`/api/auth`, `/api/register`, `/api/login`)

Аутентификация и регистрация.

| Метод | Эндпоинт | Описание |
|-------|----------|----------|
| `POST` | `/api/register` | Регистрация нового пользователя |
| `POST` | `/api/login` | Вход (возвращает JWT) |
| `POST` | `/api/auth/refresh` | Обновить JWT по refresh-токену |
| `POST` | `/api/auth/logout` | Выход (инвалидация токена) |


##  Postman Collection (JSON)

```bash
{
  "info": {
    "name": "Тыжпрограммист API",
    "description": "Интернет-магазин компьютерной техники - API коллекция",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json",
    "_postman_id": "tzyhprogramist-api-2024"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080/api",
      "type": "string"
    },
    {
      "key": "accessToken",
      "value": "",
      "type": "string"
    }
  ],
  "item": [
    {
      "name": "Auth Controller",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"john_doe\",\n  \"email\": \"john@example.com\",\n  \"password\": \"password123\",\n  \"firstName\": \"John\",\n  \"lastName\": \"Doe\",\n  \"phone\": \"+79001234567\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/register",
              "host": ["{{baseUrl}}"],
              "path": ["register"]
            }
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"john@example.com\",\n  \"password\": \"password123\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/login",
              "host": ["{{baseUrl}}"],
              "path": ["login"]
            }
          },
          "response": []
        },
        {
          "name": "Refresh Token",
          "request": {
            "method": "POST",
            "header": [{ "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"refreshToken\": \"{{refreshToken}}\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/auth/refresh",
              "host": ["{{baseUrl}}"],
              "path": ["auth", "refresh"]
            }
          }
        },
        {
          "name": "Logout",
          "request": {
            "method": "POST",
            "header": [
              { "key": "Authorization", "value": "Bearer {{accessToken}}" }
            ],
            "url": {
              "raw": "{{baseUrl}}/auth/logout",
              "host": ["{{baseUrl}}"],
              "path": ["auth", "logout"]
            }
          }
        }
      ]
    },
    {
      "name": "User Controller",
      "item": [
        {
          "name": "Get All Users",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users?page=0&size=10",
              "host": ["{{baseUrl}}"],
              "path": ["users"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "10" }]
            }
          }
        },
        {
          "name": "Get User by ID",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/1",
              "host": ["{{baseUrl}}"],
              "path": ["users", "1"]
            }
          }
        },
        {
          "name": "Get My Profile",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/me",
              "host": ["{{baseUrl}}"],
              "path": ["users", "me"]
            }
          }
        },
        {
          "name": "Search Users",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/search?query=john",
              "host": ["{{baseUrl}}"],
              "path": ["users", "search"],
              "query": [{ "key": "query", "value": "john" }]
            }
          }
        },
        {
          "name": "Get User Statistics",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/statistics",
              "host": ["{{baseUrl}}"],
              "path": ["users", "statistics"]
            }
          }
        },
        {
          "name": "Change User Role",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/1/role?role=MODERATOR",
              "host": ["{{baseUrl}}"],
              "path": ["users", "1", "role"],
              "query": [{ "key": "role", "value": "MODERATOR" }]
            }
          }
        },
        {
          "name": "Toggle User Active Status",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/1/active?active=false",
              "host": ["{{baseUrl}}"],
              "path": ["users", "1", "active"],
              "query": [{ "key": "active", "value": "false" }]
            }
          }
        },
        {
          "name": "Change My Password",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/me/password?oldPassword=old123&newPassword=new123",
              "host": ["{{baseUrl}}"],
              "path": ["users", "me", "password"],
              "query": [{ "key": "oldPassword", "value": "old123" }, { "key": "newPassword", "value": "new123" }]
            }
          }
        },
        {
          "name": "Delete User",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/users/1",
              "host": ["{{baseUrl}}"],
              "path": ["users", "1"]
            }
          }
        }
      ]
    },
    {
      "name": "Category Controller",
      "item": [
        {
          "name": "Get All Categories",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/categories?page=0&size=20",
              "host": ["{{baseUrl}}"],
              "path": ["categories"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "20" }]
            }
          }
        },
        {
          "name": "Get Category Tree",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/categories/tree",
              "host": ["{{baseUrl}}"],
              "path": ["categories", "tree"]
            }
          }
        },
        {
          "name": "Get Root Categories",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/categories/roots",
              "host": ["{{baseUrl}}"],
              "path": ["categories", "roots"]
            }
          }
        },
        {
          "name": "Get Category by Slug",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/categories/processors",
              "host": ["{{baseUrl}}"],
              "path": ["categories", "processors"]
            }
          }
        },
        {
          "name": "Get Category Breadcrumbs",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/categories/1/breadcrumbs",
              "host": ["{{baseUrl}}"],
              "path": ["categories", "1", "breadcrumbs"]
            }
          }
        },
        {
          "name": "Create Category",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/categories?name=Видеокарты&slug=graphics-cards&parentId=1",
              "host": ["{{baseUrl}}"],
              "path": ["categories"],
              "query": [{ "key": "name", "value": "Видеокарты" }, { "key": "slug", "value": "graphics-cards" }, { "key": "parentId", "value": "1" }]
            }
          }
        },
        {
          "name": "Update Category",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/categories/1?name=Процессоры&slug=cpus",
              "host": ["{{baseUrl}}"],
              "path": ["categories", "1"],
              "query": [{ "key": "name", "value": "Процессоры" }, { "key": "slug", "value": "cpus" }]
            }
          }
        },
        {
          "name": "Delete Category",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/categories/1",
              "host": ["{{baseUrl}}"],
              "path": ["categories", "1"]
            }
          }
        }
      ]
    },
    {
      "name": "Product Controller",
      "item": [
        {
          "name": "Get All Products",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/products?page=0&size=20",
              "host": ["{{baseUrl}}"],
              "path": ["products"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "20" }]
            }
          }
        },
        {
          "name": "Get Product by ID",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/products/1",
              "host": ["{{baseUrl}}"],
              "path": ["products", "1"]
            }
          }
        },
        {
          "name": "Get Product by Slug",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/products/slug/intel-core-i9-13900k",
              "host": ["{{baseUrl}}"],
              "path": ["products", "slug", "intel-core-i9-13900k"]
            }
          }
        },
        {
          "name": "Search Products",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/products/search?q=Intel",
              "host": ["{{baseUrl}}"],
              "path": ["products", "search"],
              "query": [{ "key": "q", "value": "Intel" }]
            }
          }
        },
        {
          "name": "Get New Products",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/products/new?limit=12",
              "host": ["{{baseUrl}}"],
              "path": ["products", "new"],
              "query": [{ "key": "limit", "value": "12" }]
            }
          }
        },
        {
          "name": "Get Bestsellers",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/products/bestsellers?limit=12",
              "host": ["{{baseUrl}}"],
              "path": ["products", "bestsellers"],
              "query": [{ "key": "limit", "value": "12" }]
            }
          }
        },
        {
          "name": "Get Products by Category",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/products/category/1",
              "host": ["{{baseUrl}}"],
              "path": ["products", "category", "1"]
            }
          }
        },
        {
          "name": "Create Product",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"Intel Core i9-13900K\",\n  \"sku\": \"CPU-INTEL-13900K\",\n  \"shortDescription\": \"Флагманский процессор Intel\",\n  \"fullDescription\": \"24 ядра, 32 потока, до 5.8 ГГц\",\n  \"price\": 58990,\n  \"quantity\": 50,\n  \"warrantyMonths\": 36,\n  \"weight\": 0.1\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/products?categoryId=1",
              "host": ["{{baseUrl}}"],
              "path": ["products"],
              "query": [{ "key": "categoryId", "value": "1" }]
            }
          }
        },
        {
          "name": "Update Product",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"Intel Core i9-13900K\",\n  \"price\": 54990,\n  \"quantity\": 45\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/products/1?categoryId=1",
              "host": ["{{baseUrl}}"],
              "path": ["products", "1"],
              "query": [{ "key": "categoryId", "value": "1" }]
            }
          }
        },
        {
          "name": "Delete Product",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/products/1",
              "host": ["{{baseUrl}}"],
              "path": ["products", "1"]
            }
          }
        }
      ]
    },
    {
      "name": "Cart Controller",
      "item": [
        {
          "name": "Get Cart",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/cart",
              "host": ["{{baseUrl}}"],
              "path": ["cart"]
            }
          }
        },
        {
          "name": "Add to Cart",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/cart/add?productId=1&quantity=1",
              "host": ["{{baseUrl}}"],
              "path": ["cart", "add"],
              "query": [{ "key": "productId", "value": "1" }, { "key": "quantity", "value": "1" }]
            }
          }
        },
        {
          "name": "Update Cart Item",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/cart/update?productId=1&quantity=3",
              "host": ["{{baseUrl}}"],
              "path": ["cart", "update"],
              "query": [{ "key": "productId", "value": "1" }, { "key": "quantity", "value": "3" }]
            }
          }
        },
        {
          "name": "Remove from Cart",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/cart/remove/1",
              "host": ["{{baseUrl}}"],
              "path": ["cart", "remove", "1"]
            }
          }
        },
        {
          "name": "Clear Cart",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/cart/clear",
              "host": ["{{baseUrl}}"],
              "path": ["cart", "clear"]
            }
          }
        }
      ]
    },
    {
      "name": "Order Controller",
      "item": [
        {
          "name": "Get My Orders",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/orders/me?page=0&size=10",
              "host": ["{{baseUrl}}"],
              "path": ["orders", "me"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "10" }]
            }
          }
        },
        {
          "name": "Get My Order by ID",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/orders/me/1",
              "host": ["{{baseUrl}}"],
              "path": ["orders", "me", "1"]
            }
          }
        },
        {
          "name": "Create Order",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"deliveryMethod\": \"COURIER\",\n  \"deliveryAddress\": \"г. Москва, ул. Тверская, д. 1\",\n  \"paymentMethod\": \"CARD_ONLINE\",\n  \"comment\": \"Позвонить за час до доставки\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/orders",
              "host": ["{{baseUrl}}"],
              "path": ["orders"]
            }
          }
        },
        {
          "name": "Cancel My Order",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/orders/me/1/cancel",
              "host": ["{{baseUrl}}"],
              "path": ["orders", "me", "1", "cancel"]
            }
          }
        },
        {
          "name": "Get All Orders (Admin)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/orders?page=0&size=20",
              "host": ["{{baseUrl}}"],
              "path": ["orders"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "20" }]
            }
          }
        },
        {
          "name": "Update Order Status (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/orders/1/status?status=PAID",
              "host": ["{{baseUrl}}"],
              "path": ["orders", "1", "status"],
              "query": [{ "key": "status", "value": "PAID" }]
            }
          }
        },
        {
          "name": "Get Order Statistics (Admin)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/orders/statistics",
              "host": ["{{baseUrl}}"],
              "path": ["orders", "statistics"]
            }
          }
        }
      ]
    },
    {
      "name": "PC Build Controller",
      "item": [
        {
          "name": "Get My Builds",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me"]
            }
          }
        },
        {
          "name": "Get My Build by ID",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me/1",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me", "1"]
            }
          }
        },
        {
          "name": "Create Build",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"name\": \"Моя игровая сборка\",\n  \"isPublic\": true\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me"]
            }
          }
        },
        {
          "name": "Add Component to Build",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me/1/components/100?quantity=1",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me", "1", "components", "100"],
              "query": [{ "key": "quantity", "value": "1" }]
            }
          }
        },
        {
          "name": "Remove Component from Build",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me/1/components/100",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me", "1", "components", "100"]
            }
          }
        },
        {
          "name": "Update Build",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me/1?name=Новое название&isPublic=true",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me", "1"],
              "query": [{ "key": "name", "value": "Новое название" }, { "key": "isPublic", "value": "true" }]
            }
          }
        },
        {
          "name": "Delete Build",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me/1",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me", "1"]
            }
          }
        },
        {
          "name": "Get Public Builds",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/pc-builds/public?page=0&size=20",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "public"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "20" }]
            }
          }
        },
        {
          "name": "Get Public Build by ID",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/pc-builds/public/1",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "public", "1"]
            }
          }
        },
        {
          "name": "Search Public Builds",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/pc-builds/public/search?q=игровая",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "public", "search"],
              "query": [{ "key": "q", "value": "игровая" }]
            }
          }
        },
        {
          "name": "Get Recent Public Builds",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/pc-builds/public/recent?limit=6",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "public", "recent"],
              "query": [{ "key": "limit", "value": "6" }]
            }
          }
        },
        {
          "name": "Get Build Total Price",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/pc-builds/me/1/total",
              "host": ["{{baseUrl}}"],
              "path": ["pc-builds", "me", "1", "total"]
            }
          }
        }
      ]
    },
    {
      "name": "Component Type Controller",
      "item": [
        {
          "name": "Get All Component Types",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types",
              "host": ["{{baseUrl}}"],
              "path": ["component-types"]
            }
          }
        },
        {
          "name": "Get Ordered Component Types",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types/ordered",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "ordered"]
            }
          }
        },
        {
          "name": "Get First Component Type",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types/first",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "first"]
            }
          }
        },
        {
          "name": "Get Last Component Type",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types/last",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "last"]
            }
          }
        },
        {
          "name": "Get Component Type by Step",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types/step/1",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "step", "1"]
            }
          }
        },
        {
          "name": "Get Total Steps",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types/total-steps",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "total-steps"]
            }
          }
        },
        {
          "name": "Check Compatibility Between Types",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types/compatibility/check?typeId1=1&typeId2=2",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "compatibility", "check"],
              "query": [{ "key": "typeId1", "value": "1" }, { "key": "typeId2", "value": "2" }]
            }
          }
        },
        {
          "name": "Get Component Types by Name",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/component-types/name/Процессор",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "name", "Процессор"]
            }
          }
        },
        {
          "name": "Create Component Type (Admin)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/component-types?name=Блок&orderStep=5",
              "host": ["{{baseUrl}}"],
              "path": ["component-types"],
              "query": [{ "key": "name", "value": "Блок питания" }, { "key": "orderStep", "value": "5" }]
            }
          }
        },
        {
          "name": "Add Compatibility Rule (Admin)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/component-types/compatibility/rule?typeId1=1&typeId2=2&compatible=true",
              "host": ["{{baseUrl}}"],
              "path": ["component-types", "compatibility", "rule"],
              "query": [{ "key": "typeId1", "value": "1" }, { "key": "typeId2", "value": "2" }, { "key": "compatible", "value": "true" }]
            }
          }
        }
      ]
    },
    {
      "name": "Repair Request Controller",
      "item": [
        {
          "name": "Get My Repair Requests",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests/me?page=0&size=10",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests", "me"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "10" }]
            }
          }
        },
        {
          "name": "Get My Repair Request by ID",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests/me/1",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests", "me", "1"]
            }
          }
        },
        {
          "name": "Create Repair Request",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"deviceType\": \"Ноутбук\",\n  \"problemDescription\": \"Не включается, нет индикации\",\n  \"estimatedPrice\": 3000\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/repair-requests",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests"]
            }
          }
        },
        {
          "name": "Cancel My Repair Request",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests/me/1/cancel",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests", "me", "1", "cancel"]
            }
          }
        },
        {
          "name": "Get All Repair Requests (Admin)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests?page=0&size=20",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests"],
              "query": [{ "key": "page", "value": "0" }, { "key": "size", "value": "20" }]
            }
          }
        },
        {
          "name": "Update Repair Status (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests/1/status?status=DIAGNOSTICS",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests", "1", "status"],
              "query": [{ "key": "status", "value": "DIAGNOSTICS" }]
            }
          }
        },
        {
          "name": "Set Estimated Price (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests/1/estimated-price?price=5000",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests", "1", "estimated-price"],
              "query": [{ "key": "price", "value": "5000" }]
            }
          }
        },
        {
          "name": "Complete Repair (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests/1/complete?finalPrice=4500",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests", "1", "complete"],
              "query": [{ "key": "finalPrice", "value": "4500" }]
            }
          }
        },
        {
          "name": "Add Master Comment (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/repair-requests/1/comment?comment=Неисправен контроллер питания",
              "host": ["{{baseUrl}}"],
              "path": ["repair-requests", "1", "comment"],
              "query": [{ "key": "comment", "value": "Неисправен контроллер питания" }]
            }
          }
        }
      ]
    },
    {
      "name": "Product Feedback Controller",
      "item": [
        {
          "name": "Get Product Reviews",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/feedbacks/products/1/reviews",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "products", "1", "reviews"]
            }
          }
        },
        {
          "name": "Get Product Questions",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/feedbacks/products/1/questions",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "products", "1", "questions"]
            }
          }
        },
        {
          "name": "Get Product Rating",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/feedbacks/products/1/rating",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "products", "1", "rating"]
            }
          }
        },
        {
          "name": "Get Product Rating Statistics",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/feedbacks/products/1/rating-stats",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "products", "1", "rating-stats"]
            }
          }
        },
        {
          "name": "Create Review",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"productId\": 1,\n  \"text\": \"Отличный процессор! Работает стабильно.\",\n  \"rating\": 5,\n  \"isQuestion\": false\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/feedbacks",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks"]
            }
          }
        },
        {
          "name": "Create Question",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"productId\": 1,\n  \"text\": \"Подходит ли этот процессор для материнской платы B660?\",\n  \"isQuestion\": true\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/feedbacks",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks"]
            }
          }
        },
        {
          "name": "Get My Feedbacks",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/feedbacks/me",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "me"]
            }
          }
        },
        {
          "name": "Delete My Feedback",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/feedbacks/me/1",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "me", "1"]
            }
          }
        },
        {
          "name": "Answer Question (Moderator)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/feedbacks/1/answer?answer=Да, подходит, требуется обновление BIOS",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "1", "answer"],
              "query": [{ "key": "answer", "value": "Да, подходит, требуется обновление BIOS" }]
            }
          }
        },
        {
          "name": "Publish Feedback (Moderator)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/feedbacks/1/publish",
              "host": ["{{baseUrl}}"],
              "path": ["feedbacks", "1", "publish"]
            }
          }
        }
      ]
    },
    {
      "name": "Chat Controller",
      "item": [
        {
          "name": "Create Chat Session",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/session?sourceUrl=/products/1",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "session"],
              "query": [{ "key": "sourceUrl", "value": "/products/1" }]
            }
          }
        },
        {
          "name": "Send Message",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"sessionId\": 1,\n  \"message\": \"Здравствуйте! Нужна помощь с выбором видеокарты\"\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/chat/message",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "message"]
            }
          }
        },
        {
          "name": "Get My Chat Sessions",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/me/sessions",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "me", "sessions"]
            }
          }
        },
        {
          "name": "Get Chat Session Messages",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/session/1/messages",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "session", "1", "messages"]
            }
          }
        },
        {
          "name": "Close My Chat Session",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/me/session/1/close",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "me", "session", "1", "close"]
            }
          }
        },
        {
          "name": "Get Pending Sessions (Consultant)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/consultant/pending",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "consultant", "pending"]
            }
          }
        },
        {
          "name": "Take Session (Consultant)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/consultant/session/1/take",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "consultant", "session", "1", "take"]
            }
          }
        },
        {
          "name": "Send Message as Consultant",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/consultant/session/1/message?message=Добрый день! Чем могу помочь?",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "consultant", "session", "1", "message"],
              "query": [{ "key": "message", "value": "Добрый день! Чем могу помочь?" }]
            }
          }
        },
        {
          "name": "Close Session (Consultant)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/chat/consultant/session/1/close",
              "host": ["{{baseUrl}}"],
              "path": ["chat", "consultant", "session", "1", "close"]
            }
          }
        }
      ]
    },
    {
      "name": "Site Settings Controller",
      "item": [
        {
          "name": "Get Public Settings",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/settings/public",
              "host": ["{{baseUrl}}"],
              "path": ["settings", "public"]
            }
          }
        },
        {
          "name": "Get All Settings (Admin)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/settings",
              "host": ["{{baseUrl}}"],
              "path": ["settings"]
            }
          }
        },
        {
          "name": "Update Settings (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }, { "key": "Content-Type", "value": "application/json" }],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"pickupAddress\": \"г. Москва, ул. Ленина, д. 10\",\n  \"pickupPhone\": \"+7 (495) 123-45-67\",\n  \"pickupWorkingHours\": \"Пн-Пт: 10:00-20:00, Сб: 11:00-18:00\",\n  \"deliveryCost\": 500,\n  \"freeDeliveryThreshold\": 3000\n}"
            },
            "url": {
              "raw": "{{baseUrl}}/settings",
              "host": ["{{baseUrl}}"],
              "path": ["settings"]
            }
          }
        },
        {
          "name": "Get Working Hours",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/settings/working-hours",
              "host": ["{{baseUrl}}"],
              "path": ["settings", "working-hours"]
            }
          }
        },
        {
          "name": "Get Delivery Info",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/settings/delivery-info?orderAmount=3500",
              "host": ["{{baseUrl}}"],
              "path": ["settings", "delivery-info"],
              "query": [{ "key": "orderAmount", "value": "3500" }]
            }
          }
        },
        {
          "name": "Calculate Delivery Cost",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/settings/calculate-delivery?orderAmount=2500",
              "host": ["{{baseUrl}}"],
              "path": ["settings", "calculate-delivery"],
              "query": [{ "key": "orderAmount", "value": "2500" }]
            }
          }
        },
        {
          "name": "Check Free Delivery",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/settings/is-free-delivery?orderAmount=4000",
              "host": ["{{baseUrl}}"],
              "path": ["settings", "is-free-delivery"],
              "query": [{ "key": "orderAmount", "value": "4000" }]
            }
          }
        }
      ]
    },
    {
      "name": "File Attachment Controller",
      "item": [
        {
          "name": "Get Product Images",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/files/products/1/images",
              "host": ["{{baseUrl}}"],
              "path": ["files", "products", "1", "images"]
            }
          }
        },
        {
          "name": "Get Product Main Image",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/files/products/1/main-image",
              "host": ["{{baseUrl}}"],
              "path": ["files", "products", "1", "main-image"]
            }
          }
        },
        {
          "name": "Get Objects Images",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/files/PRODUCT/1/images",
              "host": ["{{baseUrl}}"],
              "path": ["files", "PRODUCT", "1", "images"]
            }
          }
        },
        {
          "name": "Get File by ID",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/files/file/1",
              "host": ["{{baseUrl}}"],
              "path": ["files", "file", "1"]
            }
          }
        },
        {
          "name": "Upload Product Image (Admin)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/files/products/1/images?files=image1.jpg&files=image2.jpg",
              "host": ["{{baseUrl}}"],
              "path": ["files", "products", "1", "images"],
              "query": [{ "key": "files", "value": "image1.jpg" }, { "key": "files", "value": "image2.jpg" }]
            }
          }
        },
        {
          "name": "Set Main Image (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/files/1/main?contentType=PRODUCT&objectId=1",
              "host": ["{{baseUrl}}"],
              "path": ["files", "1", "main"],
              "query": [{ "key": "contentType", "value": "PRODUCT" }, { "key": "objectId", "value": "1" }]
            }
          }
        },
        {
          "name": "Delete File (Admin)",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/files/1",
              "host": ["{{baseUrl}}"],
              "path": ["files", "1"]
            }
          }
        },
        {
          "name": "Delete All Object Files (Admin)",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/files/PRODUCT/1",
              "host": ["{{baseUrl}}"],
              "path": ["files", "PRODUCT", "1"]
            }
          }
        },
        {
          "name": "Get File Statistics (Admin)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/files/statistics",
              "host": ["{{baseUrl}}"],
              "path": ["files", "statistics"]
            }
          }
        },
        {
          "name": "Find Orphaned Files (Admin)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/files/orphaned?daysOld=30",
              "host": ["{{baseUrl}}"],
              "path": ["files", "orphaned"],
              "query": [{ "key": "daysOld", "value": "30" }]
            }
          }
        },
        {
          "name": "Delete Orphaned Files (Admin)",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/files/orphaned?daysOld=30",
              "host": ["{{baseUrl}}"],
              "path": ["files", "orphaned"],
              "query": [{ "key": "daysOld", "value": "30" }]
            }
          }
        }
      ]
    },
    {
      "name": "Entity Relation Controller",
      "item": [
        {
          "name": "Check Product Compatibility",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/relations/compatibility/products/1/2",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "compatibility", "products", "1", "2"]
            }
          }
        },
        {
          "name": "Get Compatible Products",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/relations/compatibility/PRODUCT/1/compatible",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "compatibility", "PRODUCT", "1", "compatible"]
            }
          }
        },
        {
          "name": "Get Incompatible Products",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/relations/compatibility/PRODUCT/1/incompatible",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "compatibility", "PRODUCT", "1", "incompatible"]
            }
          }
        },
        {
          "name": "Check Component Compatibility",
          "request": {
            "method": "GET",
            "url": {
              "raw": "{{baseUrl}}/relations/compatibility/check?fromType=PRODUCT&fromId=1&toType=PRODUCT&toId=2",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "compatibility", "check"],
              "query": [{ "key": "fromType", "value": "PRODUCT" }, { "key": "fromId", "value": "1" }, { "key": "toType", "value": "PRODUCT" }, { "key": "toId", "value": "2" }]
            }
          }
        },
        {
          "name": "Get My Comparisons",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/comparisons",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "comparisons"]
            }
          }
        },
        {
          "name": "Get Comparison Content",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/comparisons/my-comparison",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "comparisons", "my-comparison"]
            }
          }
        },
        {
          "name": "Add Product to Comparison",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/comparisons/my-comparison/products/1",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "comparisons", "my-comparison", "products", "1"]
            }
          }
        },
        {
          "name": "Add PC Build to Comparison",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/comparisons/my-comparison/pc-builds/1",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "comparisons", "my-comparison", "pc-builds", "1"]
            }
          }
        },
        {
          "name": "Remove Product from Comparison",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/comparisons/my-comparison/products/1",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "comparisons", "my-comparison", "products", "1"]
            }
          }
        },
        {
          "name": "Delete Comparison",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/comparisons/my-comparison",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "comparisons", "my-comparison"]
            }
          }
        },
        {
          "name": "Add Compatibility Rule (Admin)",
          "request": {
            "method": "POST",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/compatibility/products?productId1=1&productId2=2&compatible=true",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "compatibility", "products"],
              "query": [{ "key": "productId1", "value": "1" }, { "key": "productId2", "value": "2" }, { "key": "compatible", "value": "true" }]
            }
          }
        },
        {
          "name": "Update Compatibility Rule (Admin)",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/compatibility/1?compatible=false",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "compatibility", "1"],
              "query": [{ "key": "compatible", "value": "false" }]
            }
          }
        },
        {
          "name": "Delete Compatibility Rule (Admin)",
          "request": {
            "method": "DELETE",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/relations/compatibility/PRODUCT/1",
              "host": ["{{baseUrl}}"],
              "path": ["relations", "compatibility", "PRODUCT", "1"]
            }
          }
        }
      ]
    },
    {
      "name": "Profile Controller",
      "item": [
        {
          "name": "Get My Profile",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/profile/me",
              "host": ["{{baseUrl}}"],
              "path": ["profile", "me"]
            }
          }
        },
        {
          "name": "Change Password",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/profile/me/password?oldPassword=old123&newPassword=new123",
              "host": ["{{baseUrl}}"],
              "path": ["profile", "me", "password"],
              "query": [{ "key": "oldPassword", "value": "old123" }, { "key": "newPassword", "value": "new123" }]
            }
          }
        },
        {
          "name": "Update Notification Settings",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/profile/me/notifications?enabled=true",
              "host": ["{{baseUrl}}"],
              "path": ["profile", "me", "notifications"],
              "query": [{ "key": "enabled", "value": "true" }]
            }
          }
        },
        {
          "name": "Update Chat Consent",
          "request": {
            "method": "PUT",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/profile/me/chat-consent?consent=true",
              "host": ["{{baseUrl}}"],
              "path": ["profile", "me", "chat-consent"],
              "query": [{ "key": "consent", "value": "true" }]
            }
          }
        }
      ]
    },
    {
      "name": "Admin Controller",
      "item": [
        {
          "name": "Ping (Health Check)",
          "request": {
            "method": "GET",
            "header": [{ "key": "Authorization", "value": "Bearer {{accessToken}}" }],
            "url": {
              "raw": "{{baseUrl}}/admin/ping",
              "host": ["{{baseUrl}}"],
              "path": ["admin", "ping"]
            }
          }
        }
      ]
    }
  ]
}
```
# Как запустить?:
После установки проекта:
```bash
https://github.com/dinya1234/tyzhprogramist.git
```

Необходимо запустить докерконтейнер(Доступно по localhost:8080):
```bash
docker-compose up --build
```
А так же запустить React(Доступно по localhost:3000):
```bash
cd C:\Users\Vova\tyzhprogramist\src\main\resources\static
npm install
npm run dev
```
