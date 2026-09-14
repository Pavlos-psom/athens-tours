package com.athenstours.mapper;

import com.athenstours.dto.TourGuideInsertDTO;
import com.athenstours.dto.TourGuideReadOnlyDTO;
import com.athenstours.dto.TourGuideUpdateDTO;
import com.athenstours.model.Role;
import com.athenstours.model.TourGuide;
import com.athenstours.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

/**
 * Entity <-> DTO conversion for TourGuide. Kept separate from the existing Mapper
 * (User) and TourMapper (Category/Tour) - same "new class, no merge conflicts" pattern.
 */
@Component
@RequiredArgsConstructor
public class TourGuideMapper {

    private final PasswordEncoder passwordEncoder;

    /** Builds the underlying User account (role GUIDE) that the guide will log in with. */
    public User mapToUserEntity(TourGuideInsertDTO dto, Role guideRole) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setRole(guideRole);
        return user;
    }

    /** Builds the guide profile itself, attached to an already-saved User. */
    public TourGuide mapToTourGuideEntity(TourGuideInsertDTO dto, User user) {
        TourGuide guide = new TourGuide();
        guide.setPhone(dto.phone());
        guide.setBio(dto.bio());
        guide.setLanguages(dto.languages() != null ? new HashSet<>(dto.languages()) : new HashSet<>());
        guide.setUser(user);
        return guide;
    }

    /** Applies the update DTO's fields onto an already-loaded, managed TourGuide entity. */
    public void updateTourGuideEntity(TourGuide guide, TourGuideUpdateDTO dto) {
        guide.setPhone(dto.phone());
        guide.setBio(dto.bio());
        guide.setLanguages(dto.languages() != null ? new HashSet<>(dto.languages()) : new HashSet<>());
    }

    public TourGuideReadOnlyDTO mapToTourGuideReadOnlyDTO(TourGuide guide) {
        return new TourGuideReadOnlyDTO(
                guide.getUuid().toString(),
                guide.getUser().getUsername(),
                guide.getPhone(),
                guide.getBio(),
                guide.getLanguages()
        );
    }
}
