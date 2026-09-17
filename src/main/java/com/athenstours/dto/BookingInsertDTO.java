package com.athenstours.dto;

import jakarta.validation.constraints.NotBlank;

/** The authenticated customer books exactly one seat on this schedule. */
public record BookingInsertDTO(

        @NotBlank(message = "Tour schedule uuid is required")
        String scheduleUuid
) {
}
