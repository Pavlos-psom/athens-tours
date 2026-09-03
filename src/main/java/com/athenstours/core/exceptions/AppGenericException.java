package com.athenstours.core.exceptions;

import lombok.Getter;

/** Root of every business exception. Carries a machine-readable `code` for the frontend. */
@Getter
public class AppGenericException extends Exception {

    private final String code;

    public AppGenericException(String code, String message) {
        super(message);
        this.code = code;
    }
}
