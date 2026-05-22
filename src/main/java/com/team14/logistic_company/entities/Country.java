package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing a country in the logistics company system.
 *
 * A country can contain multiple cities and is used
 * for organizing address information.
 */
@Entity
@Getter
public class Country {

    /**
     * Primary key of the country.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Name of the country.
     */
    @Setter
    @NotBlank(message = "The name of the country cannot be blank!")
    @Size(min = 3, max = 50,
            message = "The name of the country has to be between 3 and 50 characters!")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Timestamp when the country was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last country update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}