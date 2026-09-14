package com.athenstours.repository;

import com.athenstours.model.Role;
import com.athenstours.model.TourGuide;
import com.athenstours.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class TourGuideRepositoryTest {

    @Autowired
    private TourGuideRepository tourGuideRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TourGuide existingGuide;

    @BeforeEach
    void setup() {
        Role guideRole = new Role("TEST_GUIDE_ROLE_" + UUID.randomUUID());
        roleRepository.save(guideRole);

        User user = new User("test_guide_" + UUID.randomUUID(), "encoded-password", guideRole);
        userRepository.save(user);

        existingGuide = new TourGuide("+30 69X XXXXXXX", "Loves the Acropolis at sunrise",
                Set.of("Greek", "English"), user);
        tourGuideRepository.save(existingGuide);
    }

    @Test
    void findByUuidAndDeletedFalsePositive() {
        Optional<TourGuide> found = tourGuideRepository.findByUuidAndDeletedFalse(existingGuide.getUuid());
        assertTrue(found.isPresent());
        assertEquals(existingGuide.getId(), found.get().getId());
        assertEquals(2, found.get().getLanguages().size());
    }

    @Test
    void findByUuidAndDeletedFalseExcludesSoftDeleted() {
        existingGuide.softDelete();
        tourGuideRepository.save(existingGuide);

        Optional<TourGuide> found = tourGuideRepository.findByUuidAndDeletedFalse(existingGuide.getUuid());
        assertTrue(found.isEmpty());
    }

    @Test
    void findAllByDeletedFalseExcludesSoftDeleted() {
        assertEquals(1, tourGuideRepository.findAllByDeletedFalse().size());

        existingGuide.softDelete();
        tourGuideRepository.save(existingGuide);

        assertFalse(tourGuideRepository.findAllByDeletedFalse()
                .stream().anyMatch(g -> g.getId().equals(existingGuide.getId())));
    }

    @Test
    void uuidIsGeneratedOnPersist() {
        TourGuide reloaded = tourGuideRepository.findById(existingGuide.getId()).orElseThrow();
        assertTrue(reloaded.getUuid() != null);
    }
}
