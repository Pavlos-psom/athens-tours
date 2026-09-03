package com.athenstours.core.exceptions;

public class EntityNotFoundException extends AppGenericException {

    private static final String DEFAULT_CODE = "NotFound";

    public EntityNotFoundException(String entity, String message) {
        super(entity + DEFAULT_CODE, message);
    }
}
