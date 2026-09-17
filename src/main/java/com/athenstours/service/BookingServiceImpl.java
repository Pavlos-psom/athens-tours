package com.athenstours.service;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.core.exceptions.ScheduleFullException;
import com.athenstours.dto.BookingInsertDTO;
import com.athenstours.dto.BookingReadOnlyDTO;
import com.athenstours.mapper.BookingMapper;
import com.athenstours.model.Booking;
import com.athenstours.model.TourSchedule;
import com.athenstours.model.User;
import com.athenstours.repository.BookingRepository;
import com.athenstours.repository.TourScheduleRepository;
import com.athenstours.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements IBookingService {

    private final BookingRepository bookingRepository;
    private final TourScheduleRepository tourScheduleRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional(rollbackFor = {EntityNotFoundException.class, ScheduleFullException.class})
    public BookingReadOnlyDTO createBooking(BookingInsertDTO dto, String username)
            throws EntityNotFoundException, ScheduleFullException {

        TourSchedule schedule = findScheduleOrThrow(dto.scheduleUuid());
        User customer = findCustomerOrThrow(username);

        long activeBookings = bookingRepository.countByTourScheduleAndDeletedFalse(schedule);
        if (activeBookings >= schedule.getCapacity()) {
            throw new ScheduleFullException(
                    "Tour schedule '" + schedule.getUuid() + "' has no seats left");
        }

        Booking saved = bookingRepository.save(bookingMapper.mapToBookingEntity(schedule, customer));
        log.info("Created booking uuid={} schedule={} customer={}",
                saved.getUuid(), schedule.getUuid(), username);
        return bookingMapper.mapToBookingReadOnlyDTO(saved);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void cancelBooking(String uuid, String username) throws EntityNotFoundException {
        Booking booking = findOwnBookingOrThrow(uuid, username);
        booking.softDelete();
        log.info("Cancelled booking uuid={} by customer={}", booking.getUuid(), username);
    }

    @Override
    public List<BookingReadOnlyDTO> getMyBookings(String username) {
        return bookingRepository.findByCustomerUsernameAndDeletedFalse(username).stream()
                .map(bookingMapper::mapToBookingReadOnlyDTO)
                .toList();
    }

    @Override
    public List<BookingReadOnlyDTO> getAllBookings() {
        return bookingRepository.findAllByDeletedFalse().stream()
                .map(bookingMapper::mapToBookingReadOnlyDTO)
                .toList();
    }

    private TourSchedule findScheduleOrThrow(String uuid) throws EntityNotFoundException {
        UUID parsed;
        try {
            parsed = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("TourSchedule", "Tour schedule '" + uuid + "' not found");
        }
        return tourScheduleRepository.findByUuidAndDeletedFalse(parsed)
                .orElseThrow(() -> new EntityNotFoundException("TourSchedule", "Tour schedule '" + uuid + "' not found"));
    }

    private User findCustomerOrThrow(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User", "User '" + username + "' not found"));
    }

    // Ownership check baked into the lookup: a booking that exists but belongs to someone
    // else is treated the same as "not found", so we never leak whether it exists.
    private Booking findOwnBookingOrThrow(String uuid, String username) throws EntityNotFoundException {
        UUID parsed;
        try {
            parsed = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Booking", "Booking '" + uuid + "' not found");
        }
        return bookingRepository.findByUuidAndDeletedFalse(parsed)
                .filter(booking -> booking.getCustomer().getUsername().equals(username))
                .orElseThrow(() -> new EntityNotFoundException("Booking", "Booking '" + uuid + "' not found"));
    }
}
