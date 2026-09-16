package com.athenstours.service;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourScheduleInsertDTO;
import com.athenstours.dto.TourScheduleReadOnlyDTO;
import com.athenstours.dto.TourScheduleUpdateDTO;
import com.athenstours.mapper.TourScheduleMapper;
import com.athenstours.model.Tour;
import com.athenstours.model.TourGuide;
import com.athenstours.model.TourSchedule;
import com.athenstours.repository.TourGuideRepository;
import com.athenstours.repository.TourRepository;
import com.athenstours.repository.TourScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourScheduleServiceImpl implements ITourScheduleService {

    private final TourScheduleRepository tourScheduleRepository;
    private final TourRepository tourRepository;
    private final TourGuideRepository tourGuideRepository;
    private final TourScheduleMapper tourScheduleMapper;

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public TourScheduleReadOnlyDTO createTourSchedule(TourScheduleInsertDTO dto) throws EntityNotFoundException {
        Tour tour = findTourOrThrow(dto.tourUuid());
        TourGuide tourGuide = findGuideOrThrow(dto.tourGuideUuid());

        TourSchedule saved = tourScheduleRepository.save(
                tourScheduleMapper.mapToTourScheduleEntity(dto, tour, tourGuide));

        log.info("Created tour schedule uuid={} tour={} guide={}",
                saved.getUuid(), tour.getUuid(), tourGuide.getUuid());
        return tourScheduleMapper.mapToTourScheduleReadOnlyDTO(saved);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public TourScheduleReadOnlyDTO updateTourSchedule(String uuid, TourScheduleUpdateDTO dto)
            throws EntityNotFoundException {
        TourSchedule schedule = findScheduleOrThrow(uuid);
        TourGuide tourGuide = findGuideOrThrow(dto.tourGuideUuid());

        tourScheduleMapper.updateTourScheduleEntity(schedule, dto, tourGuide);
        log.info("Updated tour schedule uuid={}", schedule.getUuid());
        return tourScheduleMapper.mapToTourScheduleReadOnlyDTO(schedule);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteTourSchedule(String uuid) throws EntityNotFoundException {
        TourSchedule schedule = findScheduleOrThrow(uuid);
        schedule.softDelete();
        log.info("Soft-deleted tour schedule uuid={}", schedule.getUuid());
    }

    @Override
    public TourScheduleReadOnlyDTO getTourScheduleByUuid(String uuid) throws EntityNotFoundException {
        return tourScheduleMapper.mapToTourScheduleReadOnlyDTO(findScheduleOrThrow(uuid));
    }

    @Override
    public List<TourScheduleReadOnlyDTO> getAllTourSchedules() {
        return tourScheduleRepository.findAllByDeletedFalse().stream()
                .map(tourScheduleMapper::mapToTourScheduleReadOnlyDTO)
                .toList();
    }

    @Override
    public List<TourScheduleReadOnlyDTO> getTourSchedulesByTour(String tourUuid) {
        UUID parsed;
        try {
            parsed = UUID.fromString(tourUuid);
        } catch (IllegalArgumentException e) {
            return List.of();
        }
        return tourScheduleRepository.findByTourUuidAndDeletedFalse(parsed).stream()
                .map(tourScheduleMapper::mapToTourScheduleReadOnlyDTO)
                .toList();
    }

    @Override
    public List<TourScheduleReadOnlyDTO> getMyTourSchedules(String username) {
        return tourScheduleRepository.findByTourGuideUserUsernameAndDeletedFalse(username).stream()
                .map(tourScheduleMapper::mapToTourScheduleReadOnlyDTO)
                .toList();
    }

    private TourSchedule findScheduleOrThrow(String uuid) throws EntityNotFoundException {
        UUID parsed;
        try {
            parsed = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("TourSchedule", "Tour schedule '" + uuid + "' not found");
        }
        return tourScheduleRepository.findByUuidAndDeletedFalse(parsed)
                .orElseThrow(() -> new EntityNotFoundException("TourSchedule", "Tour schedule '" + uuid + "' not found"));
    }

    private Tour findTourOrThrow(String uuid) throws EntityNotFoundException {
        UUID parsed;
        try {
            parsed = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Tour", "Tour '" + uuid + "' not found");
        }
        return tourRepository.findByUuidAndDeletedFalse(parsed)
                .orElseThrow(() -> new EntityNotFoundException("Tour", "Tour '" + uuid + "' not found"));
    }

    private TourGuide findGuideOrThrow(String uuid) throws EntityNotFoundException {
        UUID parsed;
        try {
            parsed = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("TourGuide", "Tour guide '" + uuid + "' not found");
        }
        return tourGuideRepository.findByUuidAndDeletedFalse(parsed)
                .orElseThrow(() -> new EntityNotFoundException("TourGuide", "Tour guide '" + uuid + "' not found"));
    }
}
