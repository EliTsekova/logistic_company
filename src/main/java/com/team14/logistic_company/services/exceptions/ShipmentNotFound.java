package com.team14.logistic_company.services.exceptions;

public class ShipmentNotFound extends RuntimeException {
    public ShipmentNotFound(String message) {
        super(message);
    }
}
