package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when a username is already taken
 * or unavailable for registration.
 */
public class UsernameNotAvailable extends RuntimeException {

    /**
     * Constructs a new UsernameNotAvailable exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public UsernameNotAvailable(String message) {
        super(message);
    }
}