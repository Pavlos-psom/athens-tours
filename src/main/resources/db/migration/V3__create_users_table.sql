-- The `users` table (Spring Security principal). Plural name to sidestep any ambiguity
-- with the `USER`/`CURRENT_USER` builtins in MySQL, same as the course examples.

CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT,
    uuid       BINARY(16)   NOT NULL,
    username   VARCHAR(100) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    role_id    BIGINT       NOT NULL,
    created_at DATETIME     NULL,
    updated_at DATETIME     NULL,
    deleted    TINYINT(1)   NOT NULL DEFAULT 0,
    deleted_at DATETIME     NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_uuid UNIQUE (uuid),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE RESTRICT,
    INDEX idx_users_role (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
