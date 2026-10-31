package com.athenstours.core.exceptions;

/**
 * Thrown when a customer tries to book a TourSchedule on which they already hold an active booking.
 * Like ScheduleFullException, it is handled by the generic AppGenericException handler (400).
 */
public class AlreadyBookedException extends AppGenericException {

    private static final String DEFAULT_CODE = "AlreadyBooked";

    public AlreadyBookedException(String message) {
        super(DEFAULT_CODE, message);
    }
}