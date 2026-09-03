package com.athenstours.config;

import com.athenstours.model.Role;
import com.athenstours.model.User;
import com.athenstours.repository.RoleRepository;
import com.athenstours.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Dev convenience: guarantees exactly one ADMIN account exists, hashed with whatever
 * PasswordEncoder/bcrypt-strength the app is actually configured with (so there's no
 * hand-computed hash sitting in a migration file). Runs on every startup, idempotent.
 * Username/password below are for local development only - change or remove before
 * anything resembling a real deployment.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserSeeder implements CommandLineRunner {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "Passw0rd!";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(ADMIN_USERNAME).isPresent()) {
            return;
        }
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("ADMIN role not found - did Flyway seed data run?"));

        userRepository.save(new User(ADMIN_USERNAME, passwordEncoder.encode(ADMIN_PASSWORD), adminRole));
        log.info("Seeded default ADMIN user (username='{}', password='{}') - local dev only!",
                ADMIN_USERNAME, ADMIN_PASSWORD);
    }
}
