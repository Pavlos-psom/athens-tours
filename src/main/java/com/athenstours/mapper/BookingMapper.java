package com.athenstours.mapper;

import com.athenstours.dto.BookingReadOnlyDTO;
import com.athenstours.model.Booking;
import com.athenstours.model.TourSchedule;
import com.athenstours.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Entity <-> DTO conversion for Booking. Reuses the existing TourScheduleMapper for the
 * nested TourSchedule read DTO instead of duplicating that mapping.
 */
@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final TourScheduleMapper tourScheduleMapper;

    public Booking mapToBookingEntity(TourSchedule tourSchedule, User customer) {
        return new Booking(tourSchedule, customer);
    }

    public BookingReadOnlyDTO mapToBookingReadOnlyDTO(Booking booking) {
        return new BookingReadOnlyDTO(
                booking.getUuid().toString(),
                tourScheduleMapper.mapToTourScheduleReadOnlyDTO(booking.getTourSchedule()),
                booking.getCustomer().getUsername(),
                booking.getCreatedAt()
        );
    }
}
