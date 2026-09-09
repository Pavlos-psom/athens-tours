package com.athenstours.dto;

import java.math.BigDecimal;

public record TourReadOnlyDTO(
        String uuid,
        String name,
        String description,
        BigDecimal price,
        Integer durationMinutes,
        CategoryReadOnlyDTO category
) {
}
