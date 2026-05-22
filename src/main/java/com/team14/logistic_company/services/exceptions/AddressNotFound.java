package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when an address cannot be found in the system.
 */
public class AddressNotFound extends RuntimeException {

    /**
     * Constructs a new AddressNotFound exception for a missing address by id.
     *
     * @param id the id of the address that was not found
     */
    public AddressNotFound(Integer id) {
        super("Address not found with id: " + id);
    }

    /**
     * Constructs a new AddressNotFound exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public AddressNotFound(String message) {
        super(message);
    }
}