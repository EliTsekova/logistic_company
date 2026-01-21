package com.team14.logistic_company.dtos;

import lombok.Data;

import java.time.Instant;

import jakarta.validation.constraints.*;

@Data
public class CountryDto {
    private Integer id;

    @NotBlank(message = "The name of the country cannot be blank!")
    @Size(min = 5, max = 10, message = "The name of the country has to be between 5 and 10 characters!")
    private String name;
    private Instant createdOn;
    private Instant updatedOn;
}
