package com.athenstours.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/** Admin-only creation payload: picks an existing Tour and TourGuide by uuid. */
public record TourScheduleInsertDTO(

        @NotBlank(message = "Tour uuid is required")
        String tourUuid,

        @NotBlank(message = "Tour guide uuid is required")
        String tourGuideUuid,

        @NotNull(message = "Start date/time is required")
        @Future(message = "Start date/time must be in the future")
        LocalDateTime startsAt,

        @NotNull(message = "Capacity is required")
        @Positive(message = "Capacity must be a positive number")
        Integer capacity
) {
}
