package com.athenstours.service;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.core.exceptions.ScheduleFullException;
import com.athenstours.dto.BookingInsertDTO;
import com.athenstours.dto.BookingReadOnlyDTO;

import java.util.List;

public interface IBookingService {

    /** CREATE_BOOKING: the authenticated customer books one seat. */
    BookingReadOnlyDTO createBooking(BookingInsertDTO dto, String username)
            throws EntityNotFoundException, ScheduleFullException;

    /** VIEW_OWN_BOOKINGS: cancel one of the caller's own bookings (soft-delete). */
    void cancelBooking(String uuid, String username) throws EntityNotFoundException;

    /** VIEW_OWN_BOOKINGS: bookings belonging to the currently authenticated customer. */
    List<BookingReadOnlyDTO> getMyBookings(String username);

    /** MANAGE_BOOKINGS: every booking in the system. */
    List<BookingReadOnlyDTO> getAllBookings();
}
