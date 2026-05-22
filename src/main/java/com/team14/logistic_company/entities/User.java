package com.team14.logistic_company.entities;

import com.team14.logistic_company.entities.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Entity representing a system user in the logistics company application.
 *
 * A user can have different roles such as client, courier,
 * or office employee and is used for authentication
 * and authorization in the system.
 */
@Entity
public class User {

    /**
     * Primary key of the user.
     */
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * First name of the user.
     */
    @Getter
    @Setter
    @NotBlank(message = "First name cannot be blank!")
    @Size(max = 20,
            message = "First name has to be up to 20 characters!")
    @Column(name = "FirstName", nullable = false)
    private String firstName;

    /**
     * Last name of the user.
     */
    @Getter
    @Setter
    @NotBlank(message = "Last name cannot be blank!")
    @Size(max = 20,
            message = "Last name has to be up to 20 characters!")
    @Column(name = "LastName", nullable = false)
    private String lastName;

    /**
     * Unique username used for login.
     */
    @Getter
    @Setter
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 5, max = 20,
            message = "Username has to be between 5 and 20 characters!")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    /**
     * Encrypted user password.
     */
    @Getter
    @Setter
    @NotBlank(message = "Password cannot be blank!")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Unique email address of the user.
     */
    @Getter
    @Setter
    @NotBlank(message = "The email address cannot be blank!")
    @Email(message = "Invalid email address. Please enter a proper email address!")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * Role assigned to the user.
     */
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Timestamp when the user account was created.
     */
    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    /**
     * Timestamp of the last user update.
     */
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}