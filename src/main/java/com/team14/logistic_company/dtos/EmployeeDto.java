package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.PositionType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Data Transfer Object for Employee entity.
 *
 * <p>Represents an employee in the logistics system. Employees are responsible for
 * processing shipments, managing deliveries and operating within company offices.</p>
 *
 * <p>Each employee is linked to a system user account and assigned to a specific office.</p>
 *
 * <p>This DTO also contains additional user and office projection fields for display purposes.</p>
 */
@Data
@Setter
@Getter
public class EmployeeDto {

    /**
     * Unique identifier of the employee.
     */
    private Integer id;

    /**
     * Position type of the employee (COORDINATOR or DELIVERYMAN).
     */
    @NotNull(message = "Position type cannot be null!")
    private PositionType positionType;

    /**
     * Timestamp when the employee was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the employee was last updated.
     */
    private Instant updatedOn;

    // =========================
    // User projection fields
    // =========================

    /**
     * First name of the linked user (read-only display field).
     */
    private String userFirstName;

    /**
     * Last name of the linked user (read-only display field).
     */
    private String userLastName;

    /**
     * Full name of the linked user (computed display field).
     */
    private String userFullName;

    /**
     * Email of the linked user (read-only display field).
     */
    private String userEmail;

    /**
     * Username of the linked user (read-only display field).
     */
    private String userUsername;

    // =========================
    // Office projection fields
    // =========================

    /**
     * Title or name of the office where the employee works.
     */
    private String officeTitle;

    /**
     * Identifier of the assigned office.
     */
    private Integer officeId;

    /**
     * Identifier of the linked user account.
     */
    @NotNull(message = "User ID cannot be null!")
    private Integer userId;
}