package com.athenstours.service;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourScheduleInsertDTO;
import com.athenstours.dto.TourScheduleReadOnlyDTO;
import com.athenstours.dto.TourScheduleUpdateDTO;

import java.util.List;

public interface ITourScheduleService {

    TourScheduleReadOnlyDTO createTourSchedule(TourScheduleInsertDTO dto) throws EntityNotFoundException;

    TourScheduleReadOnlyDTO updateTourSchedule(String uuid, TourScheduleUpdateDTO dto) throws EntityNotFoundException;

    void deleteTourSchedule(String uuid) throws EntityNotFoundException;

    TourScheduleReadOnlyDTO getTourScheduleByUuid(String uuid) throws EntityNotFoundException;

    List<TourScheduleReadOnlyDTO> getAllTourSchedules();

    List<TourScheduleReadOnlyDTO> getTourSchedulesByTour(String tourUuid);

    /** VIEW_OWN_SCHEDULE: schedules assigned to the currently authenticated guide. */
    List<TourScheduleReadOnlyDTO> getMyTourSchedules(String username);
}
