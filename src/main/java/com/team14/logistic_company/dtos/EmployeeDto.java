package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.PositionType;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class EmployeeDto {
    private Integer id;
    private PositionType position;
    private Instant createdOn;
    private Instant updatedOn;

    private String userFirstName;
    private String userLastName;
    private String userFullName;
    private String userEmail;
    private String userUsername;
    private String officeTitle;

    private Integer officeId;
    private Integer userId;

    public EmployeeDto() {
    }
}
