package com.team14.logistic_company.services.exceptions;

public class UnauthorizedAccess extends RuntimeException {
    public UnauthorizedAccess(String message) {
        super(message);
    }
}
