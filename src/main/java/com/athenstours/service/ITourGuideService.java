package com.athenstours.service;

import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourGuideInsertDTO;
import com.athenstours.dto.TourGuideReadOnlyDTO;
import com.athenstours.dto.TourGuideUpdateDTO;

import java.util.List;

public interface ITourGuideService {

    TourGuideReadOnlyDTO createTourGuide(TourGuideInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException;

    TourGuideReadOnlyDTO updateTourGuide(String uuid, TourGuideUpdateDTO dto) throws EntityNotFoundException;

    void deleteTourGuide(String uuid) throws EntityNotFoundException;

    TourGuideReadOnlyDTO getTourGuideByUuid(String uuid) throws EntityNotFoundException;

    List<TourGuideReadOnlyDTO> getAllTourGuides();
}
