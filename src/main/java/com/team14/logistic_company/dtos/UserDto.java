package com.team14.logistic_company.dtos;

import com.team14.logistic_company.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Integer id;

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 5, max = 20, message = "Username has to be between 5 and 20 characters!")
    private String username;

    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Invalid email address. Please enter a proper email!")
    private String email;

    private Role role;

    @NotBlank(message = "First Name cannot be blank!")
    @Size(max = 20, message = "First name has to be up to 20 characters!")
    private String firstName;

    @NotBlank(message = "Last Name cannot be blank!")
    @Size(max = 20, message = "Last name has to be up to 20 characters!")
    private String lastName;

    public UserDto() {

    }

    public UserDto(String username, String password, String email, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    /*TODO: this can be added: public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }*/
}