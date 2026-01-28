package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
public class ShipmentStatusDto {
    private Integer id;
    private Integer shipmentId;
    private Status status;
    private String comment;
    private Instant createdOn;
    private Instant updatedOn;
}

