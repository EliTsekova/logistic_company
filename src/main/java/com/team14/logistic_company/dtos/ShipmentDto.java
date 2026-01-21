package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.Client;
import com.team14.logistic_company.entities.Employee;
import com.team14.logistic_company.entities.Office;
import lombok.Data;


import java.math.BigDecimal;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Data
public class ShipmentDto {
    @Getter
    private Integer id;

    private Employee employee;
    private Client recipient;
    private Address recipientAddress;
    private Client sender;
    private Address senderAddress;
    private Office office;
    private double weight;
    private BigDecimal price;
    private Instant createdOn;
    private Instant updatedOn;
    @Getter
    private String uniqueID;
    @Setter
    private String status;
}
