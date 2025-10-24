package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Shipment {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "EmployeeId", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "RecipientId", nullable = false)
    private Client recipient;

    @ManyToOne
    @JoinColumn(name = "RecipientAddress", nullable = false)
    private Address recipientAddress;

    @ManyToOne
    @JoinColumn(name = "SenderId", nullable = false)
    private Client sender;

    @ManyToOne
    @JoinColumn(name = "SenderAddress", nullable = false)
    private Address senderAddress;

    @ManyToOne
    @JoinColumn(name = "OfficeId", nullable = false)
    private Office office;

    @Positive(message = "The weight must be greater than 0")
    @Column(name = "Weight", nullable = false)
    private double weight;

    @Positive(message = "The price must be greater than 0")
    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    @NotBlank(message = "Unique ID cannot be blank!")
    @Size(min = 10, max = 20, message = "Unique id has to be between 5 and 30 characters!")
    @Column(name = "uniqueId", nullable = false, unique = true)
    private String uniqueId;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}