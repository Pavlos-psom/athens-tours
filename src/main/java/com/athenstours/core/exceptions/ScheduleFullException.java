package com.athenstours.core.exceptions;

/**
 * Thrown when a customer tries to book a TourSchedule that has no seats left.
 * Not caught by any specific handler in ErrorHandler - it falls through to the generic
 * AppGenericException handler, which returns 400 Bad Request with this exception's code
 * and message. No changes to ErrorHandler are needed for this.
 */
public class ScheduleFullException extends AppGenericException {

    private static final String DEFAULT_CODE = "ScheduleFull";

    public ScheduleFullException(String message) {
        super(DEFAULT_CODE, message);
    }
}
