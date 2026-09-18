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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private TourScheduleRepository tourScheduleRepository;
    @Mock private UserRepository userRepository;
    @Mock private BookingMapper bookingMapper;
    @InjectMocks private BookingServiceImpl bookingService;

    @Test
    void createBookingThrowsWhenScheduleNotFound() {
        String scheduleUuid = UUID.randomUUID().toString();
        BookingInsertDTO dto = new BookingInsertDTO(scheduleUuid);

        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(scheduleUuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(dto, "customer1"))
                .isInstanceOf(EntityNotFoundException.class);

        verify(userRepository, never()).findByUsername(any());
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBookingThrowsWhenCustomerNotFound() {
        String scheduleUuid = UUID.randomUUID().toString();
        BookingInsertDTO dto = new BookingInsertDTO(scheduleUuid);

        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(scheduleUuid)))
                .thenReturn(Optional.of(new TourSchedule()));
        when(userRepository.findByUsername("ghost"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(dto, "ghost"))
                .isInstanceOf(EntityNotFoundException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBookingThrowsScheduleFullExceptionWhenNoSeatsLeft() {
        String scheduleUuid = UUID.randomUUID().toString();
        BookingInsertDTO dto = new BookingInsertDTO(scheduleUuid);

        TourSchedule schedule = new TourSchedule();
        schedule.setCapacity(2);
        User customer = new User();

        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(scheduleUuid)))
                .thenReturn(Optional.of(schedule));
        when(userRepository.findByUsername("customer1"))
                .thenReturn(Optional.of(customer));
        when(bookingRepository.countByTourScheduleAndDeletedFalse(schedule))
                .thenReturn(2L);

        assertThatThrownBy(() -> bookingService.createBooking(dto, "customer1"))
                .isInstanceOf(ScheduleFullException.class);

        verify(bookingRepository, never()).save(any());
    }

    @Test
    void createBookingSucceedsWhenSeatIsAvailable() throws EntityNotFoundException, ScheduleFullException {
        String scheduleUuid = UUID.randomUUID().toString();
        BookingInsertDTO dto = new BookingInsertDTO(scheduleUuid);

        TourSchedule schedule = new TourSchedule();
        schedule.setCapacity(2);
        User customer = new User();
        customer.setUsername("customer1");

        Booking mappedBooking = new Booking(schedule, customer);
        Booking savedBooking = new Booking(schedule, customer);
        BookingReadOnlyDTO readOnlyDTO = new BookingReadOnlyDTO(
                UUID.randomUUID().toString(), null, "customer1", null);

        when(tourScheduleRepository.findByUuidAndDeletedFalse(UUID.fromString(scheduleUuid)))
                .thenReturn(Optional.of(schedule));
        when(userRepository.findByUsername("customer1"))
                .thenReturn(Optional.of(customer));
        when(bookingRepository.countByTourScheduleAndDeletedFalse(schedule))
                .thenReturn(1L);
        when(bookingMapper.mapToBookingEntity(schedule, customer))
                .thenReturn(mappedBooking);
        when(bookingRepository.save(mappedBooking))
                .thenReturn(savedBooking);
        when(bookingMapper.mapToBookingReadOnlyDTO(savedBooking))
                .thenReturn(readOnlyDTO);

        BookingReadOnlyDTO result = bookingService.createBooking(dto, "customer1");

        assertThat(result.customerUsername()).isEqualTo("customer1");
        verify(bookingRepository).save(mappedBooking);
    }

    @Test
    void cancelBookingThrowsWhenUuidNotFound() {
        String uuid = UUID.randomUUID().toString();
        when(bookingRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking(uuid, "customer1"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void cancelBookingThrowsWhenCallerIsNotTheOwner() {
        String uuid = UUID.randomUUID().toString();
        User owner = new User();
        owner.setUsername("realOwner");
        Booking booking = new Booking(new TourSchedule(), owner);

        when(bookingRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking(uuid, "someoneElse"))
                .isInstanceOf(EntityNotFoundException.class);

        // The booking must be untouched - a non-owner cancel attempt has no side effect.
        assertThat(booking.isDeleted()).isFalse();
    }

    @Test
    void cancelBookingSoftDeletesWhenCallerIsTheOwner() throws EntityNotFoundException {
        String uuid = UUID.randomUUID().toString();
        User owner = new User();
        owner.setUsername("realOwner");
        Booking booking = new Booking(new TourSchedule(), owner);

        when(bookingRepository.findByUuidAndDeletedFalse(UUID.fromString(uuid)))
                .thenReturn(Optional.of(booking));

        bookingService.cancelBooking(uuid, "realOwner");

        assertThat(booking.isDeleted()).isTrue();
    }
}
