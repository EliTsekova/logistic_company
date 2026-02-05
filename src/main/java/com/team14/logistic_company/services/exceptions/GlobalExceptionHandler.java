package com.team14.logistic_company.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    /* =======================
       NOT FOUND – 404
       ======================= */

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

    @ExceptionHandler(UnauthorizedAccess.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handleForbidden(UnauthorizedAccess ex) {
        return ex.getMessage();
    }

    /* =======================
       INTERNAL SERVER ERROR – 500
       ======================= */

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleGeneric(Exception ex) {
        return "Unexpected error occurred: " + ex.getMessage();
    }
}
