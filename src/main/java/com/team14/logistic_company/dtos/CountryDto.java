package com.team14.logistic_company.dtos;

import lombok.Data;
import java.time.Instant;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class CountryDto {
    private Integer id;

    @NotBlank(message = "The name of the country cannot be blank!")
    @Size(min = 3, max = 50, message = "The name of the country has to be between 3 and 50 characters!")
    private String name;

    private Instant createdOn;
    private Instant updatedOn;
}