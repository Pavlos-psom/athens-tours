package com.athenstours.service;

import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.UserInsertDTO;
import com.athenstours.dto.UserReadOnlyDTO;

public interface IUserService {

    UserReadOnlyDTO registerCustomer(UserInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException;
}
