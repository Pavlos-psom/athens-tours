package com.athenstours.dto;

import java.util.Set;

public record TourGuideReadOnlyDTO(
        String uuid,
        String username,
        String phone,
        String bio,
        Set<String> languages
) {
}
