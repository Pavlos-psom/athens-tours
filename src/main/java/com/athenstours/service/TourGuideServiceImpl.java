package com.athenstours.service;

import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourGuideInsertDTO;
import com.athenstours.dto.TourGuideReadOnlyDTO;
import com.athenstours.dto.TourGuideUpdateDTO;
import com.athenstours.mapper.TourGuideMapper;
import com.athenstours.model.Role;
import com.athenstours.model.TourGuide;
import com.athenstours.model.User;
import com.athenstours.repository.RoleRepository;
import com.athenstours.repository.TourGuideRepository;
import com.athenstours.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourGuideServiceImpl implements ITourGuideService {

    private static final String GUIDE_ROLE = "GUIDE";

    private final TourGuideRepository tourGuideRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TourGuideMapper tourGuideMapper;

    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class, EntityNotFoundException.class})
    public TourGuideReadOnlyDTO createTourGuide(TourGuideInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException {

        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new EntityAlreadyExistsException("User", "Username '" + dto.username() + "' is already taken");
        }

        Role guideRole = roleRepository.findByName(GUIDE_ROLE)
                .orElseThrow(() -> new EntityNotFoundException("Role", "Role '" + GUIDE_ROLE + "' not found"));

        // Two saves, one transaction: if anything after this point throws, both roll back
        // together - we never want a User with no matching TourGuide profile, or vice versa.
        User savedUser = userRepository.save(tourGuideMapper.mapToUserEntity(dto, guideRole));
        TourGuide savedGuide = tourGuideRepository.save(tourGuideMapper.mapToTourGuideEntity(dto, savedUser));

        log.info("Created tour guide uuid={} username={}", savedGuide.getUuid(), savedUser.getUsername());
        return tourGuideMapper.mapToTourGuideReadOnlyDTO(savedGuide);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public TourGuideReadOnlyDTO updateTourGuide(String uuid, TourGuideUpdateDTO dto) throws EntityNotFoundException {
        TourGuide guide = findGuideOrThrow(uuid);
        tourGuideMapper.updateTourGuideEntity(guide, dto);
        log.info("Updated tour guide uuid={}", guide.getUuid());
        return tourGuideMapper.mapToTourGuideReadOnlyDTO(guide);
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public void deleteTourGuide(String uuid) throws EntityNotFoundException {
        TourGuide guide = findGuideOrThrow(uuid);
        guide.softDelete();
        log.info("Soft-deleted tour guide uuid={}", guide.getUuid());
    }

    @Override
    public TourGuideReadOnlyDTO getTourGuideByUuid(String uuid) throws EntityNotFoundException {
        return tourGuideMapper.mapToTourGuideReadOnlyDTO(findGuideOrThrow(uuid));
    }

    @Override
    public List<TourGuideReadOnlyDTO> getAllTourGuides() {
        return tourGuideRepository.findAllByDeletedFalse().stream()
                .map(tourGuideMapper::mapToTourGuideReadOnlyDTO)
                .toList();
    }

    private TourGuide findGuideOrThrow(String uuid) throws EntityNotFoundException {
        UUID parsed;
        try {
            parsed = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("TourGuide", "Tour guide '" + uuid + "' not found");
        }
        return tourGuideRepository.findByUuidAndDeletedFalse(parsed)
                .orElseThrow(() -> new EntityNotFoundException("TourGuide", "Tour guide '" + uuid + "' not found"));
    }
}
