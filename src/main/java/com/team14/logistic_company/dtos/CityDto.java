package com.team14.logistic_company.dtos;

import lombok.Data;
import java.time.Instant;
import jakarta.validation.constraints.*;

@Data
public class CityDto {
    private Integer id;

    @NotBlank(message = "City name cannot be blank!")
    @Size(min = 3, max = 20, message = "City name has to be between 3 and 20 characters!")
    private String name;

    @NotNull(message = "Country ID cannot be null!")
    private Integer countryId;  // Връзка към държавата

    private Instant createdOn;
    private Instant updatedOn;
}
