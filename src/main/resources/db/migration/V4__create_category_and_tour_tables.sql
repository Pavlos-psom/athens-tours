-- Category (lookup/static_data - the "theme" of a Tour) and Tour (the bookable catalog item).
-- Every table carries the AbstractEntity auditing/soft-delete columns.

CREATE TABLE category (
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(500) NULL,
    created_at  DATETIME     NULL,
    updated_at  DATETIME     NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,
    deleted_at  DATETIME     NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT uk_category_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE tour (
    id               BIGINT AUTO_INCREMENT,
    uuid             BINARY(16)     NOT NULL,
    name             VARCHAR(150)   NOT NULL,
    description      VARCHAR(2000)  NULL,
    price            DECIMAL(8, 2)  NOT NULL,
    duration_minutes INT            NOT NULL,
    category_id      BIGINT         NOT NULL,
    created_at       DATETIME       NULL,
    updated_at       DATETIME       NULL,
    deleted          TINYINT(1)     NOT NULL DEFAULT 0,
    deleted_at       DATETIME       NULL,
    CONSTRAINT pk_tour PRIMARY KEY (id),
    CONSTRAINT uk_tour_uuid UNIQUE (uuid),
    CONSTRAINT fk_tour_category FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE RESTRICT,
    INDEX idx_tour_category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
