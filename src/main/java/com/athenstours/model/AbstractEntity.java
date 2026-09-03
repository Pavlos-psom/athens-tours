package com.athenstours.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Base class for every JPA entity: auditing (createdAt / updatedAt) + soft-delete
 * (deleted / deletedAt), same pattern taught for AbstractEntity in the course examples.
 * Requires @EnableJpaAuditing on the main application class (already set).
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity {

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    /** Marks the row as deleted without a real DELETE, so history is preserved. */
    public void softDelete() {
        this.deleted = true;
        this.deletedAt = Instant.now();
    }
}
