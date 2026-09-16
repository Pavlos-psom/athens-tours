package com.athenstours.mapper;

import com.athenstours.dto.TourScheduleInsertDTO;
import com.athenstours.dto.TourScheduleReadOnlyDTO;
import com.athenstours.dto.TourScheduleUpdateDTO;
import com.athenstours.model.Tour;
import com.athenstours.model.TourGuide;
import com.athenstours.model.TourSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Entity <-> DTO conversion for TourSchedule. Reuses the existing TourMapper/TourGuideMapper
 * for the nested Tour/TourGuide read DTOs instead of duplicating that mapping.
 */
@Component
@RequiredArgsConstructor
public class TourScheduleMapper {

    private final TourMapper tourMapper;
    private final TourGuideMapper tourGuideMapper;

    public TourSchedule mapToTourScheduleEntity(TourScheduleInsertDTO dto, Tour tour, TourGuide tourGuide) {
        TourSchedule schedule = new TourSchedule();
        schedule.setTour(tour);
        schedule.setTourGuide(tourGuide);
        schedule.setStartsAt(dto.startsAt());
        schedule.setCapacity(dto.capacity());
        return schedule;
    }

    /** Applies the update DTO's fields onto an already-loaded, managed TourSchedule entity. */
    public void updateTourScheduleEntity(TourSchedule schedule, TourScheduleUpdateDTO dto, TourGuide tourGuide) {
        schedule.setTourGuide(tourGuide);
        schedule.setStartsAt(dto.startsAt());
        schedule.setCapacity(dto.capacity());
    }

    public TourScheduleReadOnlyDTO mapToTourScheduleReadOnlyDTO(TourSchedule schedule) {
        return new TourScheduleReadOnlyDTO(
                schedule.getUuid().toString(),
                tourMapper.mapToTourReadOnlyDTO(schedule.getTour()),
                tourGuideMapper.mapToTourGuideReadOnlyDTO(schedule.getTourGuide()),
                schedule.getStartsAt(),
                schedule.getCapacity()
        );
    }
}
