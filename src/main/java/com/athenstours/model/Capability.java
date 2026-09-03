package com.athenstours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * A single fine-grained permission, e.g. MANAGE_TOURS. Roles are just named
 * bundles of capabilities (see Role.getAuthorities() usage later in security).
 */
@Entity
@Table(name = "capability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Capability extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    public Capability(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // equals/hashCode on the natural key (name) - needed because Capability
    // instances live inside Sets (Role.capabilities).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Capability that)) return false;
        return name != null && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
