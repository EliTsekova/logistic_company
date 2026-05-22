package com.team14.logistic_company.dtos;

import lombok.Data;
import java.time.Instant;
import jakarta.validation.constraints.*;

/**
 * Data Transfer Object for Address entity.
 *
 * <p>Represents an address used in the logistics system for shipment delivery.
 * Each address is linked to a specific city and contains street and postal code information.</p>
 *
 * <p>Used for defining delivery locations for shipments and customer addresses.</p>
 *
 * <p>Includes validation constraints to ensure data integrity before persisting or processing.</p>
 */
@Data
public class AddressDto {

    /**
     * Unique identifier of the address.
     */
    private Integer id;

    /**
     * Identifier of the city to which this address belongs.
     */
    @NotNull(message = "City ID cannot be null!")
    private Integer cityId;

    /**
     * Street name and number of the address.
     */
    @NotBlank(message = "The street name cannot be blank!")
    @Size(min = 5, max = 100, message = "The street name has to be between 5 and 100 characters!")
    private String street;

    /**
     * Postal code of the address.
     */
    @NotBlank(message = "The postal code cannot be blank!")
    @Size(min = 4, max = 10, message = "The postal code has to be between 4 and 10 characters!")
    private String postalCode;

    /**
     * Timestamp when the address was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the address was last updated.
     */
    private Instant updatedOn;
}