package com.athenstours.repository;

import com.athenstours.model.TourSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TourScheduleRepository extends JpaRepository<TourSchedule, Long> {

    Optional<TourSchedule> findByUuidAndDeletedFalse(UUID uuid);

    List<TourSchedule> findAllByDeletedFalse();

    List<TourSchedule> findByTourUuidAndDeletedFalse(UUID tourUuid);

    // Backs GET /my-schedule - a GUIDE sees only the schedules assigned to them.
    List<TourSchedule> findByTourGuideUserUsernameAndDeletedFalse(String username);
}
