package com.athenstours.service;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourInsertDTO;
import com.athenstours.dto.TourReadOnlyDTO;
import com.athenstours.dto.TourUpdateDTO;
import com.athenstours.mapper.TourMapper;
import com.athenstours.model.Tour;
import com.athenstours.model.static_data.Category;
import com.athenstours.repository.CategoryRepository;
import com.athenstours.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourServiceImpl implements ITourService {

    private final TourRepository tourRepository;
    private final CategoryRepository categoryRepository;
    private final TourMapper tourMapper;

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public TourReadOnlyDTO createTour(TourInsertDTO dto) throws EntityNotFoundException {
        Category category = findCategoryOrThrow(dto.categoryId());
        Tour saved = tourRepository.save(tourMapper.mapToTourEntity(dto, category));
        log.info("Created tour uuid={} name={}", saved.getUuid(), saved.getName());
        return tourMapper.mapToTourReadOnlyDTO(saved);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public TourReadOnlyDTO updateTour(String uuid, TourUpdateDTO dto) throws EntityNotFoundException {
        Tour tour = findTourOrThrow(uuid);
        Category category = findCategoryOrThrow(dto.categoryId());
        tourMapper.updateTourEntity(tour, dto, category);
        log.info("Updated tour uuid={}", tour.getUuid());
        return tourMapper.mapToTourReadOnlyDTO(tour);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteTour(String uuid) throws EntityNotFoundException {
        Tour tour = findTourOrThrow(uuid);
        tour.softDelete();
        log.info("Soft-deleted tour uuid={}", tour.getUuid());
    }

    @Override
    public TourReadOnlyDTO getTourByUuid(String uuid) throws EntityNotFoundException {
        return tourMapper.mapToTourReadOnlyDTO(findTourOrThrow(uuid));
    }

    @Override
    public List<TourReadOnlyDTO> getAllTours() {
        return tourRepository.findAllByDeletedFalse().stream()
                .map(tourMapper::mapToTourReadOnlyDTO)
                .toList();
    }

    @Override
    public List<TourReadOnlyDTO> getToursByCategory(Long categoryId) {
        return tourRepository.findByCategoryIdAndDeletedFalse(categoryId).stream()
                .map(tourMapper::mapToTourReadOnlyDTO)
                .toList();
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

    private Category findCategoryOrThrow(Long categoryId) throws EntityNotFoundException {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category", "Category with id " + categoryId + " not found"));
    }
}
