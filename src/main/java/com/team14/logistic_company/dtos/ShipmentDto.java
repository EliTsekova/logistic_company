package com.team14.logistic_company.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class ShipmentDto {

    private Integer id;

    private Integer employeeId;
    private Integer senderId;
    private Integer recipientId;

    private Integer senderAddressId;
    private Integer recipientAddressId;

    private Integer officeId;

    private double weight;
    private BigDecimal price;

    private String uniqueId;

    // удобство за показване (не се пази в Shipment)
    private String currentStatus;

    private Instant createdOn;
    private Instant updatedOn;
}

