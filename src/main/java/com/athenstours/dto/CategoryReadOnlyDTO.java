package com.athenstours.dto;

/** Categories are a small, seeded lookup set - the plain numeric id is fine to expose here. */
public record CategoryReadOnlyDTO(Long id, String name, String description) {
}
