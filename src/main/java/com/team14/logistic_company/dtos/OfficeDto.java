package com.team14.logistic_company.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

/**
 * Data Transfer Object for Office entity.
 *
 * <p>Represents a physical office location of the logistics company.
 * Offices are used for processing shipments, serving clients and handling deliveries.</p>
 *
 * <p>Each office is linked to an address and may be associated with a city.</p>
 *
 * <p>Used for managing company locations and operational points.</p>
 */
@Data
public class OfficeDto {

    /**
     * Unique identifier of the office.
     */
    private Integer id;

    /**
     * Title or name of the office.
     */
    @NotBlank(message = "Title cannot be blank!")
    @Size(min = 5, max = 50, message = "Title has to be between 5 and 50 characters!")
    private String title;

    /**
     * Identifier of the address where the office is located.
     */
    @NotNull(message = "Address ID cannot be null!")
    private Integer addressId;

    /**
     * Identifier of the city where the office is located.
     */
    private Integer cityId;

    /**
     * Timestamp when the office was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the office was last updated.
     */
    private Instant updatedOn;
}