package com.athenstours.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Public self-registration payload. New users always get the CUSTOMER role. */
public record UserInsertDTO(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
        String username,

        @NotBlank(message = "Password is required")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "Password must be at least 8 characters and contain at least one letter and one digit"
        )
        String password
) {
}
