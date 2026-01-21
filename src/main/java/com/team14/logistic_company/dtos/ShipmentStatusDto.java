package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.Status;

import java.time.Instant;

public class ShipmentStatusDto {
    private Integer id;
    private Integer shipmentId;
    private Status status;
    private String comment;
    private Instant createdOn;
    private Instant updatedOn;
}

