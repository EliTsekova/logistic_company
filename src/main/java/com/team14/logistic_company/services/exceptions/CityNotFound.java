package com.team14.logistic_company.services.exceptions;

public class CityNotFound extends RuntimeException {
    public CityNotFound(Integer id) {
        super("City not found with id: " + id);
    }

    public CityNotFound(String message) {
        super(message);
    }
}
