package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing a company office in the logistics system.
 *
 * An office has a title and a physical address where
 * shipments can be sent or received.
 */
@Entity
@Getter
public class Office {

    /**
     * Primary key of the office.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Office title or identifier.
     */
    @Setter
    @NotBlank(message = "Title cannot be blank!")
    @Size(min = 5, max = 20,
            message = "Title has to be between 5 and 20 characters!")
    @Column(name = "Title", nullable = false)
    private String title;

    /**
     * Physical address of the office.
     */
    @Setter
    @OneToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    /**
     * Timestamp when the office was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last office update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}