package com.athenstours.mapper;

import com.athenstours.dto.CategoryReadOnlyDTO;
import com.athenstours.dto.TourInsertDTO;
import com.athenstours.dto.TourReadOnlyDTO;
import com.athenstours.dto.TourUpdateDTO;
import com.athenstours.model.Tour;
import com.athenstours.model.static_data.Category;
import org.springframework.stereotype.Component;

/**
 * Entity <-> DTO conversion for Category/Tour. Kept separate from the existing Mapper
 * (which handles User) so this new class is a pure addition - nothing there needs editing.
 */
@Component
public class TourMapper {

    public Tour mapToTourEntity(TourInsertDTO dto, Category category) {
        Tour tour = new Tour();
        tour.setName(dto.name());
        tour.setDescription(dto.description());
        tour.setPrice(dto.price());
        tour.setDurationMinutes(dto.durationMinutes());
        tour.setCategory(category);
        return tour;
    }

    /** Applies the update DTO's fields onto an already-loaded, managed Tour entity. */
    public void updateTourEntity(Tour tour, TourUpdateDTO dto, Category category) {
        tour.setName(dto.name());
        tour.setDescription(dto.description());
        tour.setPrice(dto.price());
        tour.setDurationMinutes(dto.durationMinutes());
        tour.setCategory(category);
    }

    public TourReadOnlyDTO mapToTourReadOnlyDTO(Tour tour) {
        return new TourReadOnlyDTO(
                tour.getUuid().toString(),
                tour.getName(),
                tour.getDescription(),
                tour.getPrice(),
                tour.getDurationMinutes(),
                mapToCategoryReadOnlyDTO(tour.getCategory())
        );
    }

    public CategoryReadOnlyDTO mapToCategoryReadOnlyDTO(Category category) {
        return new CategoryReadOnlyDTO(category.getId(), category.getName(), category.getDescription());
    }
}
