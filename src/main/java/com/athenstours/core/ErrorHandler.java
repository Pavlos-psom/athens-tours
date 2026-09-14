package com.athenstours.core;

import com.athenstours.core.exceptions.AppGenericException;
import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.access.AccessDeniedException;


import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler - the single place that turns exceptions into HTTP responses,
 * so controllers/services never need try/catch. Auth-specific 401/403 are handled separately
 * in CustomAuthenticationEntryPoint / CustomAccessDeniedHandler, since those run inside the
 * security filter chain, before this advice ever sees the request.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {



    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDenied(AccessDeniedException e) throws AccessDeniedException {
        throw e;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(EntityNotFoundException e) {
        log.warn("Entity not found: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleAlreadyExists(EntityAlreadyExistsException e) {
        log.warn("Entity already exists: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(e.getCode(), e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AppGenericException.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericAppException(AppGenericException e) {
        log.warn("Application error: {}", e.getMessage());
        return new ResponseEntity<>(new ErrorResponseDTO(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        log.warn("Validation failed: {}", errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpected(Exception e) {
        log.error("Unexpected error", e);
        return new ResponseEntity<>(
                new ErrorResponseDTO("INTERNAL_ERROR", "An unexpected error occurred"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
