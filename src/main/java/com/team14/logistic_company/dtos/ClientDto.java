package com.team14.logistic_company.dtos;

import java.time.Instant;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for Client entity.
 *
 * <p>Represents a client in the logistics system who can send and receive shipments.
 * Each client is linked to a system user account.</p>
 *
 * <p>Clients can view their own shipments, track delivery status and manage personal data.</p>
 *
 * <p>This DTO also includes read-only user information for display purposes.</p>
 */
@Data
@Setter
@Getter
public class ClientDto {

    /**
     * Unique identifier of the client.
     */
    private Integer id;

    /**
     * Identifier of the associated user account.
     */
    @NotNull(message = "User ID cannot be null!")
    private Integer userId;

    /**
     * Client phone number (exactly 10 digits).
     */
    @NotBlank(message = "Phone number cannot be blank!")
    @Size(min = 10, max = 10, message = "Phone number has to be exactly 10 characters!")
    private String phoneNumber;

    /**
     * Timestamp when the client was created.
     */
    private Instant createdOn;

    /**
     * Timestamp when the client was last updated.
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
}