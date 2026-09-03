package com.athenstours.service;

import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.UserInsertDTO;
import com.athenstours.dto.UserReadOnlyDTO;
import com.athenstours.mapper.Mapper;
import com.athenstours.model.Role;
import com.athenstours.model.User;
import com.athenstours.repository.RoleRepository;
import com.athenstours.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private static final String CUSTOMER_ROLE = "CUSTOMER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Mapper mapper;

    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public UserReadOnlyDTO registerCustomer(UserInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException {

        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new EntityAlreadyExistsException("User", "Username '" + dto.username() + "' is already taken");
        }

        Role customerRole = roleRepository.findByName(CUSTOMER_ROLE)
                .orElseThrow(() -> new EntityNotFoundException("Role", "Role '" + CUSTOMER_ROLE + "' not found"));

        User saved = userRepository.save(mapper.mapToUserEntity(dto, customerRole));
        log.info("Registered new customer with username={}", saved.getUsername());
        return mapper.mapToUserReadOnlyDTO(saved);
    }
}
