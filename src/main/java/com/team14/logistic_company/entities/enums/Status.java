package com.team14.logistic_company.entities.enums;

/**
 * Enumeration representing the different statuses
 * of a shipment during the delivery process.
 *
 * Shipment statuses are used to track the progress
 * of deliveries from submission to final delivery.
 */
public enum Status {

    /**
     * Shipment has been registered in the system.
     */
    SUBMITTED,

    /**
     * Shipment has been processed by the logistics company.
     */
    PROCESSED,

    /**
     * Shipment has been shipped from the office or warehouse.
     */
    SHIPPED,

    /**
     * Shipment is currently in transit.
     */
    IN_TRANSIT,

    /**
     * Shipment is out for final delivery by the courier.
     */
    OUT_FOR_DELIVERY,

    /**
     * Shipment has been successfully delivered.
     */
    DELIVERED
}