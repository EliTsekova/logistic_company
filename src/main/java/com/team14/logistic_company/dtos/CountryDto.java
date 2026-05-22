package com.team14.logistic_company.dtos;

import lombok.Data;
import java.time.Instant;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Country entity.
 *
 * <p>Represents a country in the logistics system. Countries are used as the highest-level
 * geographic classification and are linked to cities and addresses.</p>
 *
 * <p>Used as a reference entity for organizing location-based data in the system.</p>
 */
@Data
@Setter
@Getter
public class CountryDto {

    /**
     * Unique identifier of the country.
     */
    private Integer id;

    /**
     * Name of the country.
     */
    @NotBlank(message = "The name of the country cannot be blank!")
    @Size(min = 3, max = 50, message = "The name of the country has to be between 3 and 50 characters!")
    private String name;

    /**
     * Timestamp when the country was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the country was last updated.
     */
    private Instant updatedOn;
}