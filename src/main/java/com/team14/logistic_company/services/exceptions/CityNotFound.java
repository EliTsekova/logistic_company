package com.team14.logistic_company.services.exceptions;

public class CityNotFound extends RuntimeException {
    public CityNotFound(String message) {
        super(message);
    }
}
