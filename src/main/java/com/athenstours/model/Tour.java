package com.athenstours.model;

import com.athenstours.model.static_data.Category;
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

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * A bookable tour "product" - a themed walking route with a fixed price and duration.
 * Specific bookable dates/guides come later (TourSchedule); this is just the catalog item.
 */
@Entity
@Table(name = "tour")
@Getter
@Setter
@NoArgsConstructor
public class Tour extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Tour(String name, String description, BigDecimal price, Integer durationMinutes, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationMinutes = durationMinutes;
        this.category = category;
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
        if (!(o instanceof Tour tour)) return false;
        if (uuid == null || tour.uuid == null) return false;
        return uuid.equals(tour.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
