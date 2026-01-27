package com.team14.logistic_company.dtos;
import java.time.Instant;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClientDto {
    private Integer id;

    @NotNull(message = "User ID cannot be null!")
    private Integer userId;

    @NotBlank(message = "Phone number cannot be blank!")
    @Size(min = 10, max = 10, message = "Phone number has to be exactly 10 characters!")
    private String phoneNumber;

    private Instant createdOn;
    private Instant updatedOn;

    // User информация (за показване)
    private String userFirstName;
    private String userLastName;
    private String userFullName;
    private String userEmail;
    private String userUsername;
}