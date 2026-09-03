package com.athenstours.repository;

import com.athenstours.model.Capability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CapabilityRepository extends JpaRepository<Capability, Long> {

    Optional<Capability> findByName(String name);
}
