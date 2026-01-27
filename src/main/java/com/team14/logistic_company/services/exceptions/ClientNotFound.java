package com.team14.logistic_company.services.exceptions;

public class ClientNotFound extends RuntimeException {
    public ClientNotFound(Integer id) {
        super("Client not found with id: " + id);
    }

    public ClientNotFound(String message) {
        super(message);
    }
}