package com.athenstours.repository;

import com.athenstours.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Optional<Tour> findByUuidAndDeletedFalse(UUID uuid);

    List<Tour> findByCategoryIdAndDeletedFalse(Long categoryId);

    List<Tour> findAllByDeletedFalse();
}