package com.athenstours.repository;

import com.athenstours.model.Capability;
import com.athenstours.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Repository-slice test (H2 in-memory DB, transaction rolled back after each test) -
 * same @DataJpaTest pattern used in the course examples.
 */
@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CapabilityRepository capabilityRepository;

    private Role existingRole;

    @BeforeEach
    void setup() {
        existingRole = new Role("TEST_ROLE_" + UUID.randomUUID());
        roleRepository.save(existingRole);
    }

    @Test
    void findByNamePositive() {
        Optional<Role> found = roleRepository.findByName(existingRole.getName());
        assertTrue(found.isPresent());
        assertEquals(existingRole.getId(), found.get().getId());
    }

    @Test
    void findByNameNegative() {
        Optional<Role> found = roleRepository.findByName("DOES_NOT_EXIST_" + UUID.randomUUID());
        assertTrue(found.isEmpty());
    }

    @Test
    void persistRoleWithCapabilities() {
        Capability capability = new Capability("TEST_CAP_" + UUID.randomUUID(), "test capability");
        capabilityRepository.save(capability);

        Role role = new Role("ANOTHER_ROLE_" + UUID.randomUUID());
        role.addCapability(capability);
        roleRepository.save(role);

        Role found = roleRepository.findById(role.getId()).orElseThrow();
        assertEquals(1, found.getCapabilities().size());
        assertTrue(found.getCapabilities().contains(capability));
    }
}
