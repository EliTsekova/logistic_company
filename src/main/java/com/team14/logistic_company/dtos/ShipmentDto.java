package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.DeliveryType;
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

    // read-only: calculated by the system
    private BigDecimal price;

    private DeliveryType deliveryType;

    private String uniqueId;

    private String currentStatus;

    private Instant createdOn;
    private Instant updatedOn;
}
