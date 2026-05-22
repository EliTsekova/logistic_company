package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when a client cannot be found in the system.
 */
public class ClientNotFound extends RuntimeException {

    /**
     * Constructs a new ClientNotFound exception for a missing client by id.
     *
     * @param id the id of the client that was not found
     */
    public ClientNotFound(Integer id) {
        super("Client not found with id: " + id);
    }

    /**
     * Constructs a new ClientNotFound exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public ClientNotFound(String message) {
        super(message);
    }
}