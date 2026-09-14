package com.athenstours.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Updates only the guide profile fields - username/password of the linked User are
 * not touched here (that would be a separate, future "manage my account" flow).
 * The uuid identifying which TourGuide to update comes from the URL path.
 */
public record TourGuideUpdateDTO(

        @Size(max = 30, message = "Phone must be at most 30 characters")
        String phone,

        @Size(max = 1000, message = "Bio must be at most 1000 characters")
        String bio,

        Set<@NotBlank(message = "Language must not be blank") String> languages
) {
}
