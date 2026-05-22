package com.team14.logistic_company.controllers.forms;

import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.enums.PositionType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
/**
 * Form object used for updating existing employee data.
 * Contains validation rules and helper methods for converting
 * form data into DTO objects.
 */
@Getter
@Setter
public class UpdateEmployeeForm {

    /**
     * Employee identifier.
     */
    private Integer id;

    /**
     * Related user account identifier.
     */
    private Integer userId;

    /**
     * Employee first name.
     */
    @NotBlank(message = "First name cannot be blank!")
    private String firstName;

    /**
     * Employee last name.
     */
    @NotBlank(message = "Last name cannot be blank!")
    private String lastName;

    /**
     * Username used for authentication.
     */
    @NotBlank(message = "Username cannot be blank!")
    private String username;

    /**
     * Employee email address.
     */
    @Email(message = "Email should be valid!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    /**
     * Optional password used during employee update.
     * If left blank, the current password remains unchanged.
     */
    private String password;

    /**
     * Employee position type.
     */
    @NotNull(message = "Position type cannot be null!")
    private PositionType positionType;

    /**
     * Office identifier assigned to the employee.
     */
    @NotNull(message = "Office cannot be null!")
    private Integer officeId;

    /**
     * Converts the form data into an {@link EmployeeDto} object.
     *
     * @return populated employee DTO
     */
    public EmployeeDto toEmployeeDto() {

        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(this.id);
        employeeDto.setUserId(this.userId);
        employeeDto.setPositionType(this.positionType);
        employeeDto.setOfficeId(this.officeId);

        return employeeDto;
    }

    /**
     * Converts the form data into a {@link UserDto} object.
     * Updates the password only if a new password is provided.
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

        if (this.password != null && !this.password.isBlank()) {
            userDto.setPassword(this.password);
        }

        return userDto;
    }
}