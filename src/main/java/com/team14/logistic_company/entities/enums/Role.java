package com.team14.logistic_company.entities.enums;

/**
 * Enumeration representing the different user roles
 * in the logistics company system.
 *
 * Roles determine the level of access and permissions
 * available to system users.
 */
public enum Role {

    /**
     * System administrator with full access to all functionalities.
     */
    ADMIN,

    /**
     * Company employee with access to shipment
     * and office management functionalities.
     */
    EMPLOYEE,

    /**
     * Client user who can send, receive,
     * and track shipments.
     */
    CLIENT
}