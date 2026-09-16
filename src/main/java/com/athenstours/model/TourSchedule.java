package com.athenstours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * A specific bookable occurrence of a Tour - "this Tour, on this date/time, led by
 * this TourGuide, with this many seats". The Tour itself stays a reusable catalog item.
 */
@Entity
@Table(name = "tour_schedule")
@Getter
@Setter
@NoArgsConstructor
public class TourSchedule extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guide_id", nullable = false)
    private TourGuide tourGuide;

    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    public TourSchedule(Tour tour, TourGuide tourGuide, LocalDateTime startsAt, Integer capacity) {
        this.tour = tour;
        this.tourGuide = tourGuide;
        this.startsAt = startsAt;
        this.capacity = capacity;
    }

    @PrePersist
    private void initializeUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    // equals/hashCode on the natural key (uuid) - null-safe for transient instances.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourSchedule schedule)) return false;
        if (uuid == null || schedule.uuid == null) return false;
        return uuid.equals(schedule.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
