-- TourGuide: a profile attached 1:1 to an existing `users` row (role GUIDE).
-- guide_languages is an @ElementCollection table (just strings, no id of its own).

CREATE TABLE guide (
    id         BIGINT AUTO_INCREMENT,
    uuid       BINARY(16)    NOT NULL,
    phone      VARCHAR(30)   NULL,
    bio        VARCHAR(1000) NULL,
    user_id    BIGINT        NOT NULL,
    created_at DATETIME      NULL,
    updated_at DATETIME      NULL,
    deleted    TINYINT(1)    NOT NULL DEFAULT 0,
    deleted_at DATETIME      NULL,
    CONSTRAINT pk_guide PRIMARY KEY (id),
    CONSTRAINT uk_guide_uuid UNIQUE (uuid),
    CONSTRAINT uk_guide_user UNIQUE (user_id),
    CONSTRAINT fk_guide_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE guide_languages (
    guide_id BIGINT      NOT NULL,
    language VARCHAR(50) NOT NULL,
    CONSTRAINT pk_guide_languages PRIMARY KEY (guide_id, language),
    CONSTRAINT fk_guide_languages_guide FOREIGN KEY (guide_id) REFERENCES guide (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
