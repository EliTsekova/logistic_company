package com.team14.logistic_company.dtos;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import jakarta.validation.constraints.*;

@Getter
@Setter
public class OfficeDto {
    private Integer id;

    @NotBlank(message = "Title cannot be blank!")
    @Size(min = 5, max = 20, message = "Title has to be between 5 and 20 characters!")
    private String title;
    private Instant createdOn;
    private Instant updatedOn;
}
