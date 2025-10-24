package com.team14.logistic_company.entities;

import com.team14.logistic_company.entities.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Setter
    @NotBlank(message = "First name cannot be blank!")
    @Size(max = 20, message = "First name has to be up to 20 characters!")
    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Getter
    @Setter
    @NotBlank(message = "Last name cannot be blank!")
    @Size(max = 20, message = "Last name has to be up to 20 characters!")
    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Getter
    @Setter
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 5, max = 20, message = "Username has to be between 5 and 20 characters!")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Getter
    @Setter
    @NotBlank(message = "Password cannot be blank!")
    @Column(name = "password", nullable = false)
    private String password;

    @Getter
    @Setter
    @NotBlank(message = "The email address cannot be blank!")
    @Email(message = "Invalid email address. Please enter a proper email address!")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;
    @UpdateTimestamp
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn;
}