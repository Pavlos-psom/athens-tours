package com.athenstours.repository;

import com.athenstours.model.static_data.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category existingCategory;

    @BeforeEach
    void setup() {
        existingCategory = new Category("TEST_CATEGORY_" + UUID.randomUUID(), "a test category");
        categoryRepository.save(existingCategory);
    }

    @Test
    void findByIdPositive() {
        Category found = categoryRepository.findById(existingCategory.getId()).orElseThrow();
        assertEquals(existingCategory.getName(), found.getName());
    }

    @Test
    void findAllIncludesSavedCategory() {
        assertTrue(categoryRepository.findAll().stream()
                .anyMatch(c -> c.getId().equals(existingCategory.getId())));
    }
}
