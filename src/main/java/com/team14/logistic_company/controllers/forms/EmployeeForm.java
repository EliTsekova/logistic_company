package com.team14.logistic_company.controllers.forms;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.entities.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
/**
 * Form object used for creating and editing employee data.
 * Contains validation rules and helper methods for converting
 * form data into DTO objects.
 */
@Getter
@Setter
public class EmployeeForm {

    /**
     * Employee first name.
     */
    @NotBlank(message = "First name cannot be blank!")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    private String firstName;

    /**
     * Employee last name.
     */
    @NotBlank(message = "Last name cannot be blank!")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
    private String lastName;

    /**
     * Employee position type.
     */
    @NotNull(message = "Position type cannot be null!")
    private PositionType positionType;

    /**
     * Office identifier assigned to the employee.
     * Can be null for deliverymen.
     */
    private Integer officeId;

    /**
     * Username used for authentication.
     */
    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters!")
    private String username;

    /**
     * Employee email address.
     */
    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Email should be valid!")
    private String email;

    /**
     * User password used during account creation.
     */
    private String password;

    /**
     * Password confirmation used for validation during registration.
     */
    private String confirmPassword;

    /**
     * Converts the form data into an {@link EmployeeDto} object.
     *
     * @return populated employee DTO
     */
    public EmployeeDto toEmployeeDto() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setPositionType(this.positionType);
        employeeDto.setOfficeId(this.officeId);
        return employeeDto;
    }

    /**
     * Converts the form data into a {@link UserDto} object.
     * The created user is automatically assigned the EMPLOYEE role.
     *
     * @return populated user DTO
     */
    public UserDto toUserDto() {
        UserDto userDto = new UserDto();
        userDto.setRole(Role.EMPLOYEE);
        userDto.setFirstName(this.firstName);
        userDto.setLastName(this.lastName);
        userDto.setUsername(this.username);
        userDto.setEmail(this.email);
        userDto.setPassword(this.password);
        return userDto;
    }
}