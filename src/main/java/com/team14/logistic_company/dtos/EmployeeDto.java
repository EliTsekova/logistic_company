package com.team14.logistic_company.dtos;
import com.team14.logistic_company.entities.enums.PositionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class EmployeeDto {
    private Integer id;

    @NotNull(message = "Position type cannot be null!")
    private PositionType positionType;

    private Instant createdOn;
    private Instant updatedOn;

    // User информация (за показване)
    private String userFirstName;
    private String userLastName;
    private String userFullName;
    private String userEmail;
    private String userUsername;

    // Office информация (за показване)
    private String officeTitle;

    // Foreign keys
    private Integer officeId;

    @NotNull(message = "User ID cannot be null!")
    private Integer userId;
}