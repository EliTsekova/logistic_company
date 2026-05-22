package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when an office cannot be found in the system.
 */
public class OfficeNotFound extends RuntimeException {

    /**
     * Constructs a new OfficeNotFound exception for a missing office by id.
     *
     * @param id the id of the office that was not found
     */
    public OfficeNotFound(Integer id) {
        super("Office not found with id: " + id);
    }

    /**
     * Constructs a new OfficeNotFound exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public OfficeNotFound(String message) {
        super(message);
    }
}