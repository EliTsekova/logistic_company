package com.team14.logistic_company.services.exceptions;

public class UsernameNotAvailable extends RuntimeException {
    public UsernameNotAvailable(String message) {
        super(message);
    }
}
