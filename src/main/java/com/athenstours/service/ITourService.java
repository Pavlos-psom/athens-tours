package com.athenstours.service;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourInsertDTO;
import com.athenstours.dto.TourReadOnlyDTO;
import com.athenstours.dto.TourUpdateDTO;

import java.util.List;

public interface ITourService {

    TourReadOnlyDTO createTour(TourInsertDTO dto) throws EntityNotFoundException;

    TourReadOnlyDTO updateTour(String uuid, TourUpdateDTO dto) throws EntityNotFoundException;

    void deleteTour(String uuid) throws EntityNotFoundException;

    TourReadOnlyDTO getTourByUuid(String uuid) throws EntityNotFoundException;

    List<TourReadOnlyDTO> getAllTours();

    List<TourReadOnlyDTO> getToursByCategory(Long categoryId);
}
