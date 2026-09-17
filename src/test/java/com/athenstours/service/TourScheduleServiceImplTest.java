package com.athenstours.service;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourScheduleInsertDTO;
import com.athenstours.dto.TourScheduleUpdateDTO;
import com.athenstours.mapper.TourScheduleMapper;
import com.athenstours.model.Tour;
import com.athenstours.model.TourSchedule;
import com.athenstours.repository.TourGuideRepository;
import com.athenstours.repository.TourRepository;
import com.athenstours.repository.TourScheduleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TourScheduleServiceImplTest {

    @Mock private TourScheduleRepository tourScheduleRepository;
    @Mock private TourRepository tourRepository;
    @Mock private TourGuideRepository tourGuideRepository;
    @Mock private TourScheduleMapper tourScheduleMapper;
    @InjectMocks private TourScheduleServiceImpl tourScheduleService;

    @Test
    void createTourScheduleThrowsWhenTourNotFound() {
        String tourUuid = UUID.randomUUID().toString();
        String guideUuid = UUID.randomUUID().toString();
        TourScheduleInsertDTO dto = new TourScheduleInsertDTO(
                tourUuid, guideUuid, LocalDateTime.now().plusDays(1), 10);

        when(tourRepository.findByUuidAndDeletedFalse(UUID.fromString(tourUuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourScheduleService.createTourSchedule(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(tourGuideRepository, never()).findByUuidAndDeletedFalse(any());
        verify(tourScheduleRepository, never()).save(any());
    }

    @Test
    void createTourScheduleThrowsWhenGuideNotFound() {
        String tourUuid = UUID.randomUUID().toString();
        String guideUuid = UUID.randomUUID().toString();
        TourScheduleInsertDTO dto = new TourScheduleInsertDTO(
                tourUuid, guideUuid, LocalDateTime.now().plusDays(1), 10);

        when(tourRepository.findByUuidAndDeletedFalse(UUID.fromString(tourUuid)))
                .thenReturn(Optional.of(new Tour()));
        when(tourGuideRepository.findByUuidAndDeletedFalse(UUID.fromString(guideUuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourScheduleService.createTourSchedule(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(tourScheduleRepository, never()).save(any());
    }

    @Test
    void updateTourScheduleThrowsWhenUuidNotFound() {
        String uuid = UUID.randomUUID().toString();
        String guideUuid = UUID.randomUUID().toString();
        TourScheduleUpdateDTO dto = new TourScheduleUpdateDTO(
                guideUuid, LocalDateTime.now().plusDays(1), 15);

        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourScheduleService.updateTourSchedule(uuid, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(tourGuideRepository, never()).findByUuidAndDeletedFalse(any());
    }

    @Test
    void updateTourScheduleThrowsWhenGuideNotFound() {
        String uuid = UUID.randomUUID().toString();
        String guideUuid = UUID.randomUUID().toString();
        TourScheduleUpdateDTO dto = new TourScheduleUpdateDTO(
                guideUuid, LocalDateTime.now().plusDays(1), 15);

        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.of(new TourSchedule()));
        when(tourGuideRepository.findByUuidAndDeletedFalse(UUID.fromString(guideUuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourScheduleService.updateTourSchedule(uuid, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteTourScheduleThrowsWhenUuidNotFound() {
        String uuid = UUID.randomUUID().toString();
        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourScheduleService.deleteTourSchedule(uuid))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getTourScheduleByUuidThrowsWhenUuidNotFound() {
        String uuid = UUID.randomUUID().toString();
        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> tourScheduleService.getTourScheduleByUuid(uuid))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getTourSchedulesByTourReturnsEmptyListWhenUuidIsMalformed() {
        List<?> result = tourScheduleService.getTourSchedulesByTour("not-a-valid-uuid");

        assertThat(result).isEmpty();
        verify(tourScheduleRepository, never()).findByTourUuidAndDeletedFalse(any());
    }
}
