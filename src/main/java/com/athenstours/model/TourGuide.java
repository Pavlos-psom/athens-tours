package com.athenstours.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A tour guide's profile - always attached 1:1 to an existing User account (role GUIDE).
 * The User handles login/authentication; this entity only carries the extra guide-specific
 * fields (phone, bio, languages). Created only by an ADMIN (see TourGuideServiceImpl).
 */
@Entity
@Table(name = "guide")
@Getter
@Setter
@NoArgsConstructor
public class TourGuide extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private UUID uuid;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "bio", length = 1000)
    private String bio;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "guide_languages", joinColumns = @JoinColumn(name = "guide_id"))
    @Column(name = "language", length = 50)
    private Set<String> languages = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public TourGuide(String phone, String bio, Set<String> languages, User user) {
        this.phone = phone;
        this.bio = bio;
        this.languages = languages != null ? new HashSet<>(languages) : new HashSet<>();
        this.user = user;
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
        if (!(o instanceof TourGuide tourGuide)) return false;
        if (uuid == null || tourGuide.uuid == null) return false;
        return uuid.equals(tourGuide.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
