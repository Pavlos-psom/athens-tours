-- Role / Capability (auth lookup tables) + their M:N join table.
-- Every table carries the AbstractEntity auditing/soft-delete columns.

CREATE TABLE role (
    id         BIGINT AUTO_INCREMENT,
    name       VARCHAR(50)  NOT NULL,
    created_at DATETIME     NULL,
    updated_at DATETIME     NULL,
    deleted    TINYINT(1)   NOT NULL DEFAULT 0,
    deleted_at DATETIME     NULL,
    CONSTRAINT pk_role PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE capability (
    id          BIGINT AUTO_INCREMENT,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255) NULL,
    created_at  DATETIME     NULL,
    updated_at  DATETIME     NULL,
    deleted     TINYINT(1)   NOT NULL DEFAULT 0,
    deleted_at  DATETIME     NULL,
    CONSTRAINT pk_capability PRIMARY KEY (id),
    CONSTRAINT uk_capability_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE roles_capabilities (
    role_id       BIGINT NOT NULL,
    capability_id BIGINT NOT NULL,
    CONSTRAINT pk_roles_capabilities PRIMARY KEY (role_id, capability_id),
    CONSTRAINT fk_roles_capabilities_role
        FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE,
    CONSTRAINT fk_roles_capabilities_capability
        FOREIGN KEY (capability_id) REFERENCES capability (id) ON DELETE CASCADE,
    INDEX idx_roles_capabilities_capability (capability_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
