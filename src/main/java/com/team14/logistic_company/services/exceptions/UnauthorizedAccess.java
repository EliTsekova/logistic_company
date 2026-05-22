package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when a user attempts to access
 * a resource without sufficient permissions.
 */
public class UnauthorizedAccess extends RuntimeException {

    /**
     * Constructs a new UnauthorizedAccess exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public UnauthorizedAccess(String message) {
        super(message);
    }
}