package com.athenstours.repository;

import com.athenstours.model.Tour;
import com.athenstours.model.static_data.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TourRepositoryTest {

    @Autowired
    private TourRepository tourRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Tour existingTour;

    @BeforeEach
    void setup() {
        category = new Category("TEST_CATEGORY_" + UUID.randomUUID(), "a test category");
        categoryRepository.save(category);

        existingTour = new Tour("Test Tour", "a test tour", new BigDecimal("29.90"), 120, category);
        tourRepository.save(existingTour);
    }

    @Test
    void findByUuidPositive() {
        Optional<Tour> found = tourRepository.findByUuidAndDeletedFalse(existingTour.getUuid());
        assertTrue(found.isPresent());
        assertEquals(existingTour.getId(), found.get().getId());
    }

    @Test
    void findByUuidNegative() {
        Optional<Tour> found = tourRepository.findByUuidAndDeletedFalse(UUID.randomUUID());
        assertTrue(found.isEmpty());
    }

    @Test
    void findByCategoryIdReturnsMatchingTours() {
        List<Tour> found = tourRepository.findByCategoryIdAndDeletedFalse(category.getId());
        assertEquals(1, found.size());
        assertEquals(existingTour.getId(), found.get(0).getId());
    }

    @Test
    void uuidIsGeneratedOnPersist() {
        Tour reloaded = tourRepository.findById(existingTour.getId()).orElseThrow();
        assertTrue(reloaded.getUuid() != null);
    }
}
