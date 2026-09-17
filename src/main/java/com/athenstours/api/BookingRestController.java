package com.athenstours.api;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.core.exceptions.ScheduleFullException;
import com.athenstours.dto.BookingInsertDTO;
import com.athenstours.dto.BookingReadOnlyDTO;
import com.athenstours.model.User;
import com.athenstours.service.IBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * No changes to SecurityConfiguration.java are needed for this controller: every endpoint
 * here requires authentication (there is no public GET, unlike tours/guides/schedules), and
 * that is already the default via .anyRequest().authenticated(). @PreAuthorize below handles
 * which specific capability each endpoint additionally requires.
 */
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingRestController {

    private final IBookingService bookingService;

    /** CUSTOMER-only: book one seat on a tour schedule. */
    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_BOOKING')")
    public ResponseEntity<BookingReadOnlyDTO> createBooking(@Valid @RequestBody BookingInsertDTO dto,
                                                              @AuthenticationPrincipal User currentUser)
            throws EntityNotFoundException, ScheduleFullException {
        BookingReadOnlyDTO created = bookingService.createBooking(dto, currentUser.getUsername());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /** CUSTOMER-only: "my bookings" - only the reservations the caller made. */
    @GetMapping("/my-bookings")
    @PreAuthorize("hasAuthority('VIEW_OWN_BOOKINGS')")
    public List<BookingReadOnlyDTO> getMyBookings(@AuthenticationPrincipal User currentUser) {
        return bookingService.getMyBookings(currentUser.getUsername());
    }

    /** CUSTOMER-only: cancel one of the caller's own bookings. */
    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('VIEW_OWN_BOOKINGS')")
    public ResponseEntity<Void> cancelBooking(@PathVariable String uuid,
                                               @AuthenticationPrincipal User currentUser)
            throws EntityNotFoundException {
        bookingService.cancelBooking(uuid, currentUser.getUsername());
        return ResponseEntity.noContent().build();
    }

    /** ADMIN-only: every booking in the system. */
    @GetMapping
    @PreAuthorize("hasAuthority('MANAGE_BOOKINGS')")
    public List<BookingReadOnlyDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }
}
