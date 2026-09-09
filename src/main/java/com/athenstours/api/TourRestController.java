package com.athenstours.api;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourInsertDTO;
import com.athenstours.dto.TourReadOnlyDTO;
import com.athenstours.dto.TourUpdateDTO;
import com.athenstours.service.ITourService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tours")
@RequiredArgsConstructor
public class TourRestController {

    private final ITourService tourService;

    /** Public - browsing tours doesn't require login. Optional ?categoryId= to filter. */
    @GetMapping
    public List<TourReadOnlyDTO> getAllTours(@RequestParam(required = false) Long categoryId) {
        if (categoryId != null) {
            return tourService.getToursByCategory(categoryId);
        }
        return tourService.getAllTours();
    }

    /** Public - a single tour's details page doesn't require login either. */
    @GetMapping("/{uuid}")
    public ResponseEntity<TourReadOnlyDTO> getTourByUuid(@PathVariable String uuid) throws EntityNotFoundException {
        return ResponseEntity.ok(tourService.getTourByUuid(uuid));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_TOURS')")
    public ResponseEntity<TourReadOnlyDTO> createTour(@Valid @RequestBody TourInsertDTO dto)
            throws EntityNotFoundException {
        TourReadOnlyDTO created = tourService.createTour(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('MANAGE_TOURS')")
    public ResponseEntity<TourReadOnlyDTO> updateTour(@PathVariable String uuid,
                                                        @Valid @RequestBody TourUpdateDTO dto)
            throws EntityNotFoundException {
        return ResponseEntity.ok(tourService.updateTour(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('MANAGE_TOURS')")
    public ResponseEntity<Void> deleteTour(@PathVariable String uuid) throws EntityNotFoundException {
        tourService.deleteTour(uuid);
        return ResponseEntity.noContent().build();
    }
}
