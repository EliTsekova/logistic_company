package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for User entity.
 *
 * <p>Represents a system user in the logistics application. Users can log in to the system
 * and are assigned specific roles such as ADMIN, EMPLOYEE or CLIENT.</p>
 *
 * <p>This DTO is used for authentication, user registration and user management operations.</p>
 *
 * <p>Contains personal information and security-related credentials.</p>
 */
@Getter
@Setter
public class UserDto {

    /**
     * Unique identifier of the user.
     */
    private Integer id;

    /**
     * Username used for login authentication.
     */
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 5, max = 20, message = "Username has to be between 5 and 20 characters!")
    private String username;

    /**
     * User password (used only for registration or update operations).
     */
    @NotBlank(message = "Password cannot be blank!")
    private String password;

    /**
     * Email address of the user.
     */
    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Invalid email address. Please enter a proper email!")
    private String email;

    /**
     * Role of the user in the system (ADMIN, EMPLOYEE, CLIENT).
     */
    private Role role;

    /**
     * First name of the user.
     */
    @NotBlank(message = "First Name cannot be blank!")
    @Size(max = 20, message = "First name has to be up to 20 characters!")
    private String firstName;

    /**
     * Last name of the user.
     */
    @NotBlank(message = "Last Name cannot be blank!")
    @Size(max = 20, message = "Last name has to be up to 20 characters!")
    private String lastName;

    /**
     * Default no-args constructor.
     */
    public UserDto() {
    }

    /**
     * Constructor for basic user creation.
     *
     * @param username username of the user
     * @param password password of the user
     * @param email email address of the user
     * @param role role assigned to the user
     */
    public UserDto(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }
}