-- V1__create_initial_tables.sql
-- Initial schema for asset_tracker

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ROLE_ADMIN', 'ROLE_STAFF'))
);

-- Categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Assets table
CREATE TABLE assets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(50) NOT NULL CHECK (status IN ('AVAILABLE', 'ASSIGNED', 'MAINTENANCE')),
    category_id BIGINT NOT NULL REFERENCES categories(id),
    assigned_to_id UUID REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for common queries
CREATE INDEX idx_assets_category_id ON assets(category_id);
CREATE INDEX idx_assets_assigned_to_id ON assets(assigned_to_id);
CREATE INDEX idx_assets_status ON assets(status);