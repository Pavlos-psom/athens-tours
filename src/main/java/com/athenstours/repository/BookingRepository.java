package com.athenstours.repository;

import com.athenstours.model.Booking;
import com.athenstours.model.TourSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByUuidAndDeletedFalse(UUID uuid);

    List<Booking> findAllByDeletedFalse();

    // Backs GET /my-bookings - a CUSTOMER sees only their own bookings.
    List<Booking> findByCustomerUsernameAndDeletedFalse(String username);

    // Backs the capacity check in createBooking: how many active seats are already taken.
    long countByTourScheduleAndDeletedFalse(TourSchedule tourSchedule);
}
