package com.team14.logistic_company.services.exceptions;

public class AddressNotFound extends RuntimeException {
    public AddressNotFound(Integer id) {
        super("Address not found with id: " + id);
    }

    public AddressNotFound(String message) {
        super(message);
    }
}