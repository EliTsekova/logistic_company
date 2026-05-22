package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when a shipment cannot be found in the system.
 */
public class ShipmentNotFound extends RuntimeException {

    /**
     * Constructs a new ShipmentNotFound exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public ShipmentNotFound(String message) {
        super(message);
    }
}