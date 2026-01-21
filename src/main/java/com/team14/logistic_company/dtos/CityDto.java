package com.team14.logistic_company.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public class CityDto {
    private Integer id;

    @NotBlank(message = "City name cannot be blank!")
    @Size(min = 3, max = 20, message = "City name has to be between 3 and 20 characters!")
    private String name;

    private Integer countryId;
    private Instant createdOn;
    private Instant updatedOn;
}
