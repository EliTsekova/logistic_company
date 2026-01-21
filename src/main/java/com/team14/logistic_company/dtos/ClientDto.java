package com.team14.logistic_company.dtos;


import java.time.Instant;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientDto {
    private Integer id;
    private Integer userId;

    @Size(min = 10, max = 10, message = "Phone number has to be exactly 10 characters!")
    private String phoneNumber;
    private Instant createdOn;
    private Instant updatedOn;
}
