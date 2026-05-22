package com.team14.logistic_company.dtos;

import lombok.Data;
import java.time.Instant;
import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for City entity.
 *
 * <p>Represents a city in the logistics system. Each city belongs to a country
 * and can be associated with addresses used for shipment delivery.</p>
 *
 * <p>Used as a reference entity for Address and Location-related data.</p>
 *
 * <p>Ensures validation of city name and country relationship before persistence.</p>
 */
@Data
public class CityDto {

    /**
     * Unique identifier of the city.
     */
    private Integer id;

    /**
     * Name of the city.
     */
    @NotBlank(message = "City name cannot be blank!")
    @Size(min = 3, max = 20, message = "City name has to be between 3 and 20 characters!")
    private String name;

    /**
     * Identifier of the country to which this city belongs.
     */
    @NotNull(message = "Country ID cannot be null!")
    private Integer countryId;

    /**
     * Timestamp when the city was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the city was last updated.
     */
    private Instant updatedOn;
}