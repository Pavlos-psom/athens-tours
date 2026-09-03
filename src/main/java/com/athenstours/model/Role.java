package com.athenstours.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * ADMIN / GUIDE / CUSTOMER. A Role is just a named bundle of Capabilities (M:N);
 * User (added in the next step) has a single Role.
 */
@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "roles_capabilities",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "capability_id")
    )
    private Set<Capability> capabilities = new HashSet<>();

    public Role(String name) {
        this.name = name;
    }

    public void addCapability(Capability capability) {
        capabilities.add(capability);
    }

    public void removeCapability(Capability capability) {
        capabilities.remove(capability);
    }

    // equals/hashCode on the natural key (name) - Role instances end up in Sets too.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role role)) return false;
        return name != null && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
