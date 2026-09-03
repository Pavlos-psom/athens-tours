package com.athenstours.core.exceptions;

public class EntityAlreadyExistsException extends AppGenericException {

    private static final String DEFAULT_CODE = "AlreadyExists";

    public EntityAlreadyExistsException(String entity, String message) {
        super(entity + DEFAULT_CODE, message);
    }
}
