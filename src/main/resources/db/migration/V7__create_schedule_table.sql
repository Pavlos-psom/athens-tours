-- TourSchedule: a specific bookable occurrence of a Tour - "this Tour, on this date/time,
-- led by this TourGuide, with this many seats". The Tour itself stays a reusable catalog item.

CREATE TABLE tour_schedule (
    id         BIGINT AUTO_INCREMENT,
    uuid       BINARY(16) NOT NULL,
    tour_id    BIGINT     NOT NULL,
    guide_id   BIGINT     NOT NULL,
    starts_at  DATETIME   NOT NULL,
    capacity   INT        NOT NULL,
    created_at DATETIME   NULL,
    updated_at DATETIME   NULL,
    deleted    TINYINT(1) NOT NULL DEFAULT 0,
    deleted_at DATETIME   NULL,
    CONSTRAINT pk_tour_schedule PRIMARY KEY (id),
    CONSTRAINT uk_tour_schedule_uuid UNIQUE (uuid),
    CONSTRAINT fk_tour_schedule_tour FOREIGN KEY (tour_id) REFERENCES tour (id) ON DELETE RESTRICT,
    CONSTRAINT fk_tour_schedule_guide FOREIGN KEY (guide_id) REFERENCES guide (id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
