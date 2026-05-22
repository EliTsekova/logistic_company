package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing a physical address in the logistics company system.
 *
 * An address contains information about city, street,
 * postal code, and timestamps for creation and updates.
 */
@Entity
@Getter
public class Address {

    /**
     * Primary key of the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * City associated with the address.
     */
    @Setter
    @ManyToOne
    @JoinColumn(name = "CityId", nullable = false)
    private City city;

    /**
     * Street name and number of the address.
     */
    @Setter
    @NotBlank(message = "The street name cannot be blank!")
    @Size(min = 5, max = 20,
            message = "The street name has to be between 5 and 20 characters!")
    @Column(name = "Street", nullable = false)
    private String street;

    /**
     * Postal code of the address.
     */
    @Setter
    @NotBlank(message = "The postal code cannot be blank!")
    @Column(name = "PostalCode", nullable = false)
    private String postalCode;

    /**
     * Timestamp when the address was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last address update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}