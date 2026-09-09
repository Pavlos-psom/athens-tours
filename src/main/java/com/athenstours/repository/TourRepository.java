package com.athenstours.repository;

import com.athenstours.model.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TourRepository extends JpaRepository<Tour, Long> {

    Optional<Tour> findByUuid(UUID uuid);

    List<Tour> findByCategoryId(Long categoryId);
}
