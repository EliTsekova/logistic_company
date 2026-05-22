package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing a city in the logistics company system.
 *
 * A city belongs to a country and is used in addresses,
 * offices, and shipment locations.
 */
@Entity
@Getter
public class City {

    /**
     * Primary key of the city.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Name of the city.
     */
    @Setter
    @NotBlank(message = "The city name cannot be blank!")
    @Size(min = 3, max = 20,
            message = "The city name has to be between 3 and 20 characters!")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Country to which the city belongs.
     */
    @Setter
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    /**
     * Timestamp when the city was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last city update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}