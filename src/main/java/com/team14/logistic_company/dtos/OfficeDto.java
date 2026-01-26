package com.team14.logistic_company.dtos;

import lombok.Data;
import java.time.Instant;
import jakarta.validation.constraints.*;

@Data
public class OfficeDto {
    private Integer id;

    @NotBlank(message = "Title cannot be blank!")
    @Size(min = 5, max = 50, message = "Title has to be between 5 and 50 characters!")
    private String title;

    @NotNull(message = "Address ID cannot be null!")
    private Integer addressId;

    private Instant createdOn;
    private Instant updatedOn;
}
