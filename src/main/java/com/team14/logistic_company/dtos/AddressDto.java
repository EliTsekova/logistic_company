package com.team14.logistic_company.dtos;

import lombok.Data;
import java.time.Instant;
import jakarta.validation.constraints.*;

@Data
public class AddressDto {
    private Integer id;

    @NotNull(message = "City ID cannot be null!")
    private Integer cityId;  // Връзка към града

    @NotBlank(message = "The street name cannot be blank!")
    @Size(min = 5, max = 100, message = "The street name has to be between 5 and 100 characters!")
    private String street;

    @NotBlank(message = "The postal code cannot be blank!")
    @Size(min = 4, max = 10, message = "The postal code has to be between 4 and 10 characters!")
    private String postalCode;

    private Instant createdOn;
    private Instant updatedOn;
}