package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Data Transfer Object for ShipmentStatus entity.
 *
 * <p>Represents the status history of a shipment in the logistics system.
 * Each record tracks a change in the shipment lifecycle (e.g. SUBMITTED, IN_TRANSIT, DELIVERED).</p>
 *
 * <p>Used for tracking shipment progress and maintaining audit history of status changes.</p>
 */
@Getter
@Setter
public class ShipmentStatusDto {

    /**
     * Unique identifier of the shipment status record.
     */
    private Integer id;

    /**
     * Identifier of the shipment to which this status belongs.
     */
    private Integer shipmentId;

    /**
     * Current status of the shipment.
     */
    private Status status;

    /**
     * Optional comment describing the status change.
     */
    private String comment;

    /**
     * Timestamp when the status was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the status was last updated.
     */
    private Instant updatedOn;
}