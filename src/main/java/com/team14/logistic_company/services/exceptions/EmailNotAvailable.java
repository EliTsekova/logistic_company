package com.team14.logistic_company.services.exceptions;

public class EmailNotAvailable extends RuntimeException {
    public EmailNotAvailable(String message) {
        super(message);
    }
}
