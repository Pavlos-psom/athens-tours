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

import java.util.Objects;
import java.util.UUID;

/**
 * A customer's reservation of exactly one seat on a specific TourSchedule occurrence.
 * Cancelling a booking is a soft-delete (see AbstractEntity), so past history and
 * capacity accounting are preserved instead of losing the row.
 */
@Entity
@Table(name = "booking")
@Getter
@Setter
@NoArgsConstructor
public class Booking extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", nullable = false)
    private TourSchedule tourSchedule;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    public Booking(TourSchedule tourSchedule, User customer) {
        this.tourSchedule = tourSchedule;
        this.customer = customer;
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
        if (!(o instanceof Booking booking)) return false;
        if (uuid == null || booking.uuid == null) return false;
        return uuid.equals(booking.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
