package com.team14.logistic_company.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing a client in the logistics company system.
 *
 * A client is associated with a user account and contains
 * additional client-specific information such as phone number.
 */
@Entity
@Getter
@SuppressWarnings("unused")
public class Client {

    /**
     * Primary key of the client.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * User account associated with the client.
     */
    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Client phone number.
     */
    @Setter
    @NotBlank(message = "Phone number cannot be blank!")
    @Size(min = 10, max = 10,
            message = "The phone number has to be exactly 10 characters!")
    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    /**
     * Timestamp when the client was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last client update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}