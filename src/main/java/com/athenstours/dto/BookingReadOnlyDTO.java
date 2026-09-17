package com.athenstours.dto;

import java.time.Instant;

public record BookingReadOnlyDTO(
        String uuid,
        TourScheduleReadOnlyDTO schedule,
        String customerUsername,
        Instant bookedAt
) {
}
