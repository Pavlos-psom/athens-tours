package com.athenstours.api;

import com.athenstours.core.exceptions.EntityNotFoundException;
import com.athenstours.dto.TourScheduleInsertDTO;
import com.athenstours.dto.TourScheduleReadOnlyDTO;
import com.athenstours.dto.TourScheduleUpdateDTO;
import com.athenstours.model.User;
import com.athenstours.service.ITourScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@RequestMapping("/api/v1/tour-schedules")
@RequiredArgsConstructor
public class TourScheduleRestController {

    private final ITourScheduleService tourScheduleService;

    /** Public - browsing available tour dates doesn't require login. Optional ?tourUuid= to filter. */
    @GetMapping
    public List<TourScheduleReadOnlyDTO> getAllTourSchedules(@RequestParam(required = false) String tourUuid) {
        if (tourUuid != null) {
            return tourScheduleService.getTourSchedulesByTour(tourUuid);
        }
        return tourScheduleService.getAllTourSchedules();
    }

    /** GUIDE-only: "my schedule" - only the occurrences this authenticated guide is assigned to. */
    @GetMapping("/my-schedule")
    @PreAuthorize("hasAuthority('VIEW_OWN_SCHEDULE')")
    public List<TourScheduleReadOnlyDTO> getMyTourSchedules(@AuthenticationPrincipal User currentUser) {
        return tourScheduleService.getMyTourSchedules(currentUser.getUsername());
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<TourScheduleReadOnlyDTO> getTourScheduleByUuid(@PathVariable String uuid)
            throws EntityNotFoundException {
        return ResponseEntity.ok(tourScheduleService.getTourScheduleByUuid(uuid));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANAGE_SCHEDULES')")
    public ResponseEntity<TourScheduleReadOnlyDTO> createTourSchedule(@Valid @RequestBody TourScheduleInsertDTO dto)
            throws EntityNotFoundException {
        TourScheduleReadOnlyDTO created = tourScheduleService.createTourSchedule(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{uuid}")
    @PreAuthorize("hasAuthority('MANAGE_SCHEDULES')")
    public ResponseEntity<TourScheduleReadOnlyDTO> updateTourSchedule(@PathVariable String uuid,
                                                                        @Valid @RequestBody TourScheduleUpdateDTO dto)
            throws EntityNotFoundException {
        return ResponseEntity.ok(tourScheduleService.updateTourSchedule(uuid, dto));
    }

    @DeleteMapping("/{uuid}")
    @PreAuthorize("hasAuthority('MANAGE_SCHEDULES')")
    public ResponseEntity<Void> deleteTourSchedule(@PathVariable String uuid) throws EntityNotFoundException {
        tourScheduleService.deleteTourSchedule(uuid);
        return ResponseEntity.noContent().build();
    }
}
