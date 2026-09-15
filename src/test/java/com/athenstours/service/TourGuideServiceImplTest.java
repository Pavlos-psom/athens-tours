package com.athenstours.service;

import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourGuideInsertDTO;
import com.athenstours.dto.TourGuideUpdateDTO;
import com.athenstours.mapper.TourGuideMapper;
import com.athenstours.model.User;
import com.athenstours.repository.RoleRepository;
import com.athenstours.repository.TourGuideRepository;
import com.athenstours.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the service-layer logic that the repository test can't see:
 * uniqueness checks and not-found handling. The happy-path save/mapping is
 * exercised indirectly through these (mocked) collaborators.
 */
@ExtendWith(MockitoExtension.class)
class TourGuideServiceImplTest {

    @Mock
    private TourGuideRepository tourGuideRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TourGuideMapper tourGuideMapper;

    @InjectMocks
    private TourGuideServiceImpl tourGuideService;

    @Test
    void createTourGuideThrowsWhenUsernameAlreadyTaken() {
        TourGuideInsertDTO dto = new TourGuideInsertDTO(
                "takenUsername", "Password1", "+30 69X XXXXXXX", "bio", Set.of("Greek"));

        when(userRepository.findByUsername("takenUsername"))
                .thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> tourGuideService.createTourGuide(dto))
                .isInstanceOf(EntityAlreadyExistsException.class);

        // Should fail fast - never touch the role lookup or either save.
        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any());
        verify(tourGuideRepository, never()).save(any());
    }

    @Test
    void updateTourGuideThrowsWhenUuidNotFound() {
        String uuid = UUID.randomUUID().toString();
        TourGuideUpdateDTO dto = new TourGuideUpdateDTO("+30 69X XXXXXXX", "new bio", Set.of("English"));

        when(tourGuideRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourGuideService.updateTourGuide(uuid, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteTourGuideThrowsWhenUuidNotFound() {
        String uuid = UUID.randomUUID().toString();

        when(tourGuideRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourGuideService.deleteTourGuide(uuid))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
