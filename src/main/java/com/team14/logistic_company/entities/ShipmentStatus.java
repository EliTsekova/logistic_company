package com.team14.logistic_company.entities;

import com.team14.logistic_company.entities.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing the status history of a shipment.
 *
 * Shipment statuses are used to track the current state
 * and progress of a shipment during the delivery process.
 */
@Getter
@Setter
@Entity
public class ShipmentStatus {

    /**
     * Primary key of the shipment status.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Shipment associated with the status.
     */
    @ManyToOne
    @JoinColumn(name = "ShipmentId", nullable = false)
    private Shipment shipment;

    /**
     * Current shipment status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private Status status;

    /**
     * Additional comment related to the shipment status.
     */
    @Column(name = "comment")
    private String comment;

    /**
     * Timestamp when the shipment status was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last shipment status update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}