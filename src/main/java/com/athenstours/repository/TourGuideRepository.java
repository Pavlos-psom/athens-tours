package com.athenstours.repository;

import com.athenstours.model.TourGuide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TourGuideRepository extends JpaRepository<TourGuide, Long> {

    // AndDeletedFalse from day one this time - no repeat of the Tour soft-delete gap.
    Optional<TourGuide> findByUuidAndDeletedFalse(UUID uuid);

    List<TourGuide> findAllByDeletedFalse();
}
