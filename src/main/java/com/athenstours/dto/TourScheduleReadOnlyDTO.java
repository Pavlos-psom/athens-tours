package com.athenstours.dto;

import java.time.LocalDateTime;

public record TourScheduleReadOnlyDTO(
        String uuid,
        TourReadOnlyDTO tour,
        TourGuideReadOnlyDTO tourGuide,
        LocalDateTime startsAt,
        Integer capacity
) {
}
