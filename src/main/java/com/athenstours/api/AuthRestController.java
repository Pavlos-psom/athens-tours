package com.athenstours.api;

import com.athenstours.authentication.JwtService;
import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.AuthenticationRequestDTO;
import com.athenstours.dto.AuthenticationResponseDTO;
import com.athenstours.dto.UserInsertDTO;
import com.athenstours.dto.UserReadOnlyDTO;
import com.athenstours.model.User;
import com.athenstours.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /** Public self-registration - always creates a CUSTOMER. */
    @PostMapping("/register")
    public ResponseEntity<UserReadOnlyDTO> register(@Valid @RequestBody UserInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        UserReadOnlyDTO created = userService.registerCustomer(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(authentication.getName(), user.getRole().getName());
        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
    }
}
