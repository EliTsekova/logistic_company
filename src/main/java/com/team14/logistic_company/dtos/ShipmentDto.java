package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.DeliveryType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Data Transfer Object for Shipment entity.
 *
 * <p>Represents a shipment in the logistics system. A shipment contains all information
 * related to sending and delivering parcels between senders and recipients.</p>
 *
 * <p>Includes sender, recipient, employee handling the shipment, deliveryman,
 * addresses, pricing, delivery type and status information.</p>
 *
 * <p>This DTO is the core of the logistics workflow and is used in shipment creation,
 * tracking, updating status and reporting.</p>
 */
@Getter
@Setter
public class ShipmentDto {

    /**
     * Unique identifier of the shipment.
     */
    private Integer id;

    // =========================
    // Employees and participants
    // =========================

    /**
     * ID of the employee (coordinator) handling the shipment.
     */
    private Integer employeeId;

    /**
     * ID of the deliveryman assigned to the shipment.
     */
    private Integer deliverymanId;

    /**
     * ID of the registered sender (client).
     */
    private Integer senderId;

    /**
     * Optional ID of the registered recipient (if exists in system).
     */
    private Integer recipientId;

    // =========================
    // Sender address data
    // =========================

    /**
     * ID of the sender address entity.
     */
    private Integer senderAddressId;

    /**
     * Sender city ID (used for manual address composition).
     */
    private Integer senderCityId;

    /**
     * Sender street name.
     */
    private String senderStreet;

    /**
     * Sender postal code.
     */
    private String senderPostalCode;

    // =========================
    // Recipient address data
    // =========================

    /**
     * ID of the recipient address entity.
     */
    private Integer recipientAddressId;

    /**
     * Recipient city ID (used for manual address composition).
     */
    private Integer recipientCityId;

    /**
     * Recipient street name.
     */
    private String recipientStreet;

    /**
     * Recipient postal code.
     */
    private String recipientPostalCode;

    // =========================
    // Manual recipient data
    // =========================

    /**
     * Name of the recipient (used when recipient is not a registered user).
     */
    private String recipientName;

    /**
     * Phone number of the recipient.
     */
    private String recipientPhone;

    // =========================
    // Office data
    // =========================

    /**
     * ID of the office where shipment is processed or delivered.
     */
    private Integer officeId;

    // =========================
    // Display / projection fields
    // =========================

    /**
     * Full name of the employee handling the shipment.
     */
    private String employeeName;

    /**
     * Full name of the deliveryman.
     */
    private String deliverymanName;

    /**
     * Full name of the sender.
     */
    private String senderName;

    /**
     * Formatted sender address text.
     */
    private String senderAddressText;

    /**
     * Formatted recipient address text.
     */
    private String recipientAddressText;

    /**
     * Title of the office handling the shipment.
     */
    private String officeTitle;

    // =========================
    // Shipment data
    // =========================

    /**
     * Weight of the shipment in kilograms.
     */
    private double weight;

    /**
     * Price of the shipment.
     */
    private BigDecimal price;

    /**
     * Delivery type (TO_OFFICE or TO_ADDRESS).
     */
    private DeliveryType deliveryType;

    /**
     * Unique tracking identifier for the shipment.
     */
    private String uniqueId;

    /**
     * Current status of the shipment (e.g. SUBMITTED, IN_TRANSIT, DELIVERED).
     */
    private String currentStatus;

    /**
     * Timestamp when the shipment was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the shipment was last updated.
     */
    private Instant updatedOn;
}