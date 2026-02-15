CREATE TABLE tool_category (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tool_category_name VARCHAR(255) NOT NULL UNIQUE,
    active             BOOLEAN      NOT NULL DEFAULT TRUE
);
