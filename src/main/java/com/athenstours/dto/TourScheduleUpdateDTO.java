package com.athenstours.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * Updates the guide, time and capacity of an existing schedule. The tour it belongs to
 * is fixed at creation - to move a schedule to a different tour, delete and recreate it.
 */
public record TourScheduleUpdateDTO(

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
