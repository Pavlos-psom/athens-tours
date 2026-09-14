package com.athenstours.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Admin-only creation payload: this creates BOTH the underlying User account (role GUIDE)
 * and the guide profile in one call - a guide never self-registers.
 */
public record TourGuideInsertDTO(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "Password must be at least 8 characters and contain at least one letter and one digit"
        )
        String password,

        @Size(max = 30, message = "Phone must be at most 30 characters")
        String phone,

        @Size(max = 1000, message = "Bio must be at most 1000 characters")
        String bio,

        Set<@NotBlank(message = "Language must not be blank") String> languages
) {
}
