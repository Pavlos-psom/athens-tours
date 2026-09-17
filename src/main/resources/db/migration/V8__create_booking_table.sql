CREATE TABLE booking (
    id          BIGINT AUTO_INCREMENT,
    uuid        BINARY(16) NOT NULL,
    schedule_id BIGINT     NOT NULL,
    customer_id BIGINT     NOT NULL,
    created_at  DATETIME   NULL,
    updated_at  DATETIME   NULL,
    deleted     TINYINT(1) NOT NULL DEFAULT 0,
    deleted_at  DATETIME   NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT uk_booking_uuid UNIQUE (uuid),
    CONSTRAINT fk_booking_schedule FOREIGN KEY (schedule_id) REFERENCES tour_schedule (id) ON DELETE RESTRICT,
    CONSTRAINT fk_booking_customer FOREIGN KEY (customer_id) REFERENCES users (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
