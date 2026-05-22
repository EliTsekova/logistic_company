package com.team14.logistic_company.controllers.forms;

import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.dtos.UserDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
/**
 * Form object used for updating existing client data.
 * Contains validation rules and helper methods for converting
 * form data into DTO objects.
 */
@Getter
@Setter
public class UpdateClientForm {

    /**
     * Client identifier.
     */
    private Integer id;

    /**
     * Related user account identifier.
     */
    private Integer userId;

    /**
     * Client first name.
     */
    @NotBlank(message = "First name cannot be blank!")
    private String firstName;

    /**
     * Client last name.
     */
    @NotBlank(message = "Last name cannot be blank!")
    private String lastName;

    /**
     * Username used for authentication.
     */
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    /**
     * Client email address.
     */
    @Email(message = "Email should be valid!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    /**
     * Client phone number.
     */
    @NotBlank(message = "Phone number cannot be blank!")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 characters!")
    private String phoneNumber;

    /**
     * Converts the form data into a {@link ClientDto} object.
     *
     * @return populated client DTO
     */
    public ClientDto toClientDto() {
        ClientDto clientDto = new ClientDto();

        clientDto.setId(this.id);
        clientDto.setUserId(this.userId);
        clientDto.setPhoneNumber(this.phoneNumber);

        return clientDto;
    }

    /**
     * Converts the form data into a {@link UserDto} object.
     *
     * @return populated user DTO
     */
    public UserDto toUserDto() {
        UserDto userDto = new UserDto();

        userDto.setId(this.userId);
        userDto.setFirstName(this.firstName);
        userDto.setLastName(this.lastName);
        userDto.setUsername(this.username);
        userDto.setEmail(this.email);

        return userDto;
    }
}