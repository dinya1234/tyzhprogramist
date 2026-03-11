--liquibase formatted sql

-- ==================== USERS TABLE ====================
--changeset tyzhprogramist:1
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(255),
    avatar VARCHAR(255),
    date_joined TIMESTAMP NOT NULL,
    last_activity TIMESTAMP,
    notifications BOOLEAN DEFAULT TRUE NOT NULL,
    consent_to_chat_data BOOLEAN DEFAULT FALSE,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE
);

-- ==================== CATEGORIES TABLE ====================
--changeset tyzhprogramist:2
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    slug VARCHAR(255) NOT NULL UNIQUE,
    parent_id BIGINT,
    image VARCHAR(255),
    sort_order INT,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- ==================== PRODUCTS TABLE ====================
--changeset tyzhprogramist:3
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    sku VARCHAR(255) NOT NULL UNIQUE,
    category_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    short_description TEXT,
    full_description TEXT,
    price DECIMAL(10,2) NOT NULL,
    old_price DECIMAL(10,2),
    quantity INT NOT NULL,
    rating DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL,
    is_new BOOLEAN NOT NULL,
    is_bestseller BOOLEAN NOT NULL,
    views_count INT DEFAULT 0,
    purchase_count INT DEFAULT 0,
    warranty_months INT,
    weight_kg DECIMAL(10,2),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- ==================== CARTS TABLE ====================
--changeset tyzhprogramist:4
CREATE TABLE carts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT UNIQUE,
    session_key VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==================== CHAT SESSIONS TABLE ====================
--changeset tyzhprogramist:5
CREATE TABLE chat_sessions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    consultant_id BIGINT,
    status VARCHAR(20) NOT NULL,
    source_url VARCHAR(255),
    context_content_type VARCHAR(50),
    context_object_id BIGINT,
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (consultant_id) REFERENCES users(id)
);

-- ==================== CHAT MESSAGES TABLE ====================
--changeset tyzhprogramist:6
CREATE TABLE chat_messages (
    id BIGSERIAL PRIMARY KEY,
    session_id BIGINT NOT NULL,
    sender_type VARCHAR(20) NOT NULL,
    message TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    FOREIGN KEY (session_id) REFERENCES chat_sessions(id)
);

-- ==================== COMPONENT TYPES TABLE ====================
--changeset tyzhprogramist:7
CREATE TABLE component_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    order_step INT NOT NULL
);

-- ==================== ENTITY RELATIONS TABLE ====================
--changeset tyzhprogramist:8
CREATE TABLE entity_relations (
    id BIGSERIAL PRIMARY KEY,
    relation_type VARCHAR(20) NOT NULL,
    user_id BIGINT,
    from_content_type VARCHAR(50) NOT NULL,
    from_object_id BIGINT NOT NULL,
    to_content_type VARCHAR(50) NOT NULL,
    to_object_id BIGINT NOT NULL,
    is_compatible BOOLEAN,
    name VARCHAR(200),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==================== FILE ATTACHMENTS TABLE ====================
--changeset tyzhprogramist:9
CREATE TABLE file_attachments (
    id BIGSERIAL PRIMARY KEY,
    content_type VARCHAR(100) NOT NULL,
    object_id BIGINT NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    original_filename VARCHAR(255),
    file_size BIGINT,
    mime_type VARCHAR(100),
    is_main BOOLEAN DEFAULT FALSE,
    sort_order INT,
    uploaded_at TIMESTAMP NOT NULL
);

-- ==================== ORDERS TABLE ====================
--changeset tyzhprogramist:10
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    delivery_method VARCHAR(255) NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    comment TEXT,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==================== PC BUILDS TABLE ====================
--changeset tyzhprogramist:11
CREATE TABLE pc_builds (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    is_public BOOLEAN NOT NULL,
    views_count INT DEFAULT 0 NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==================== PRODUCT FEEDBACK TABLE ====================
--changeset tyzhprogramist:12
CREATE TABLE product_feedback (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    feedback_type VARCHAR(20) NOT NULL,
    rating INT,
    text TEXT NOT NULL,
    answer TEXT,
    is_published BOOLEAN DEFAULT FALSE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==================== PRODUCT ITEMS TABLE ====================
--changeset tyzhprogramist:13
CREATE TABLE product_items (
    id BIGSERIAL PRIMARY KEY,
    parent_type VARCHAR(20) NOT NULL,
    parent_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1 NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- ==================== REFRESH TOKENS TABLE ====================
--changeset tyzhprogramist:14
CREATE TABLE refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    token VARCHAR(512) NOT NULL UNIQUE,
    expiry_date TIMESTAMP NOT NULL,
    revoked BOOLEAN DEFAULT FALSE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==================== REPAIR REQUESTS TABLE ====================
--changeset tyzhprogramist:15
CREATE TABLE repair_requests (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    device_type VARCHAR(100) NOT NULL,
    problem_description VARCHAR(2000) NOT NULL,
    status VARCHAR(30) NOT NULL,
    master_comment VARCHAR(2000),
    estimated_price DECIMAL(10,2),
    final_price DECIMAL(10,2),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ==================== SITE SETTINGS TABLE ====================
--changeset tyzhprogramist:16
CREATE TABLE site_settings (
    id BIGSERIAL PRIMARY KEY,
    pickup_address VARCHAR(255) NOT NULL,
    pickup_phone VARCHAR(20) NOT NULL,
    pickup_working_hours VARCHAR(255) NOT NULL,
    delivery_cost DECIMAL(10,2) NOT NULL,
    free_delivery_threshold DECIMAL(10,2) NOT NULL
);

-- ==================== RELATED PRODUCTS (MANY-TO-MANY) ====================
--changeset tyzhprogramist:17
CREATE TABLE related_products (
    product_id BIGINT NOT NULL,
    related_product_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, related_product_id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (related_product_id) REFERENCES products(id)
);

-- ==================== FREQUENTLY BOUGHT TOGETHER ====================
--changeset tyzhprogramist:18
CREATE TABLE frequently_bought_together (
    product_id BIGINT NOT NULL,
    related_product_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, related_product_id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (related_product_id) REFERENCES products(id)
);

-- ==================== INDEXES ====================
--changeset tyzhprogramist:19
-- Users indexes
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);

-- Products indexes
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_products_slug ON products(slug);
CREATE INDEX idx_products_sku ON products(sku);
CREATE INDEX idx_products_price ON products(price);
CREATE INDEX idx_products_rating ON products(rating);
CREATE INDEX idx_products_active ON products(is_active, is_new, is_bestseller);

-- Categories indexes
CREATE INDEX idx_categories_slug ON categories(slug);
CREATE INDEX idx_categories_parent ON categories(parent_id);

-- Orders indexes
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created ON orders(created_at);

-- Chat indexes
CREATE INDEX idx_chat_sessions_user ON chat_sessions(user_id);
CREATE INDEX idx_chat_sessions_consultant ON chat_sessions(consultant_id);
CREATE INDEX idx_chat_sessions_status ON chat_sessions(status);
CREATE INDEX idx_chat_messages_session ON chat_messages(session_id);
CREATE INDEX idx_chat_messages_timestamp ON chat_messages(timestamp);

-- File attachments indexes
CREATE INDEX idx_file_attachments_content ON file_attachments(content_type, object_id);

-- Entity relations indexes
CREATE INDEX idx_entity_relations_from ON entity_relations(from_content_type, from_object_id);
CREATE INDEX idx_entity_relations_to ON entity_relations(to_content_type, to_object_id);
CREATE INDEX idx_entity_relations_user ON entity_relations(user_id);

-- Product items indexes
CREATE INDEX idx_product_items_parent ON product_items(parent_type, parent_id);
CREATE INDEX idx_product_items_product ON product_items(product_id);

-- Product feedback indexes
CREATE INDEX idx_product_feedback_product ON product_feedback(product_id);
CREATE INDEX idx_product_feedback_user ON product_feedback(user_id);

-- PC Builds indexes
CREATE INDEX idx_pc_builds_user ON pc_builds(user_id);
CREATE INDEX idx_pc_builds_public ON pc_builds(is_public, views_count);