package com.team14.logistic_company.controllers.forms;
import com.team14.logistic_company.dtos.ClientDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientForm {

    @NotBlank(message = "First name cannot be blank!")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank!")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
    private String lastName;

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters!")
    private String username;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Email should be valid!")
    private String email;

    @NotBlank(message = "Phone number cannot be blank!")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 characters!")
    private String phoneNumber;

    // Пароли - ще се използват само при създаване
    private String password;
    private String confirmPassword;

    // Конвертиране към ClientDto
    public ClientDto toClientDto() {
        ClientDto clientDto = new ClientDto();
        clientDto.setPhoneNumber(this.phoneNumber);
        return clientDto;
    }

    // Конвертиране към UserDto
    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setRole(Role.CLIENT);
        userDto.setFirstName(this.firstName);
        userDto.setLastName(this.lastName);
        userDto.setUsername(this.username);
        userDto.setEmail(this.email);
        userDto.setPassword(this.password);
        return userDto;
    }
}