package com.team14.logistic_company.services.exceptions;

/**
 * Exception thrown when a city cannot be found in the system.
 */
public class CityNotFound extends RuntimeException {

    /**
     * Constructs a new CityNotFound exception for a missing city by id.
     *
     * @param id the id of the city that was not found
     */
    public CityNotFound(Integer id) {
        super("City not found with id: " + id);
    }

    /**
     * Constructs a new CityNotFound exception with a custom message.
     *
     * @param message detailed message describing the exception
     */
    public CityNotFound(String message) {
        super(message);
    }
}