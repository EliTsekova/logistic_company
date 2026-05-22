package com.team14.logistic_company.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Global exception handler for the application.
 * Handles application-specific and generic exceptions
 * and maps them to appropriate HTTP status codes.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /* =======================
       NOT FOUND – 404
       ======================= */

    /**
     * Handles exceptions thrown when an entity cannot be found.
     *
     * @param ex the thrown exception
     * @return the exception message
     */
    @ExceptionHandler({
            ClientNotFound.class,
            UserNotFound.class,
            EmployeeNotFound.class,
            CityNotFound.class,
            CountryNotFound.class,
            OfficeNotFound.class,
            ShipmentNotFound.class,
            AddressNotFound.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFound(RuntimeException ex) {
        return ex.getMessage();
    }

    /* =======================
       BAD REQUEST – 400
       ======================= */

    /**
     * Handles exceptions caused by invalid client requests
     * or unavailable user credentials.
     *
     * @param ex the thrown exception
     * @return the exception message
     */
    @ExceptionHandler({
            EmailNotAvailable.class,
            UsernameNotAvailable.class,
            IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleBadRequest(RuntimeException ex) {
        return ex.getMessage();
    }

    /* =======================
       FORBIDDEN – 403
       ======================= */

    /**
     * Handles unauthorized access exceptions.
     *
     * @param ex the thrown exception
     * @return the exception message
     */
    @ExceptionHandler(UnauthorizedAccess.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handleForbidden(UnauthorizedAccess ex) {
        return ex.getMessage();
    }

    /* =======================
       INTERNAL SERVER ERROR – 500
       ======================= */

    /**
     * Handles all uncaught exceptions in the application.
     *
     * @param ex the thrown exception
     * @return a generic error message
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleGeneric(Exception ex) {
        return "Unexpected error occurred: " + ex.getMessage();
    }
}