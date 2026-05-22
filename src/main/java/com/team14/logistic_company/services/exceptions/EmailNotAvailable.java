package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when an email address is already in use
 * or unavailable for registration.
 */
public class EmailNotAvailable extends RuntimeException {

    /**
     * Constructs a new EmailNotAvailable exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public EmailNotAvailable(String message) {
        super(message);
    }
}