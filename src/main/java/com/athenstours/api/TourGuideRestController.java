package com.athenstours.api;

import com.athenstours.core.exceptions.EntityAlreadyExistsException;
import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourGuideInsertDTO;
import com.athenstours.dto.TourGuideReadOnlyDTO;
import com.athenstours.dto.TourGuideUpdateDTO;
import com.athenstours.service.ITourGuideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tour-guides")
@RequiredArgsConstructor
public class TourGuideRestController {

    private final ITourGuideService tourGuideService;

    /** Public - a "meet our guides" page doesn't require login. */
    @GetMapping
    public List<TourGuideReadOnlyDTO> getAllTourGuides() {
        return tourGuideService.getAllTourGuides();
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TourGuideReadOnlyDTO> getTourGuideByUuid(@PathVariable String uuid)
            throws EntityNotFoundException {
        return ResponseEntity.ok(tourGuideService.getTourGuideByUuid(uuid));
    }

    /** Admin-only: creates the guide's User account (role GUIDE) AND profile together. */
    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_GUIDES')")
    public ResponseEntity<TourGuideReadOnlyDTO> createTourGuide(@Valid @RequestBody TourGuideInsertDTO dto)
            throws EntityAlreadyExistsException, EntityNotFoundException {
        TourGuideReadOnlyDTO created = tourGuideService.createTourGuide(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('MANAGE_GUIDES')")
    public ResponseEntity<TourGuideReadOnlyDTO> updateTourGuide(@PathVariable String uuid,
                                                                  @Valid @RequestBody TourGuideUpdateDTO dto)
            throws EntityNotFoundException {
        return ResponseEntity.ok(tourGuideService.updateTourGuide(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('MANAGE_GUIDES')")
    public ResponseEntity<Void> deleteTourGuide(@PathVariable String uuid) throws EntityNotFoundException {
        tourGuideService.deleteTourGuide(uuid);
        return ResponseEntity.noContent().build();
    }
}
