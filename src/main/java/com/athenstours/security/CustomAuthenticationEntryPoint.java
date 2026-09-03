package com.athenstours.security;

import com.athenstours.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 401 handler. Security filters run before controllers and before the global
 * @RestControllerAdvice, so the JSON body has to be written manually here.
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                          AuthenticationException authException) throws IOException {
        log.warn("Unauthenticated request to {}: {}", request.getRequestURI(), authException.getMessage());

        String code = switch (authException.getClass().getSimpleName()) {
            case "BadCredentialsException" -> "BAD_CREDENTIALS";
            case "CredentialsExpiredException" -> "CREDENTIALS_EXPIRED";
            case "DisabledException" -> "ACCOUNT_DISABLED";
            case "LockedException" -> "ACCOUNT_LOCKED";
            default -> "UNAUTHORIZED";
        };

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(
                objectMapper.writeValueAsString(new ErrorResponseDTO(code, authException.getMessage())));
    }
}
