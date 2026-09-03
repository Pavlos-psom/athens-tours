package com.athenstours.mapper;

import com.athenstours.dto.UserInsertDTO;
import com.athenstours.dto.UserReadOnlyDTO;
import com.athenstours.model.Role;
import com.athenstours.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Entity <-> DTO conversion, kept in one place so nothing else touches passwords directly. */
@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;

    public User mapToUserEntity(UserInsertDTO dto, Role role) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(role);
        return user;
    }

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        return new UserReadOnlyDTO(user.getUuid().toString(), user.getUsername(), user.getRole().getName());
    }
}
