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

@Getter
@Setter
public class EmployeeForm {

    @NotBlank(message = "First name cannot be blank!")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters!")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank!")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters!")
    private String lastName;

    @NotNull(message = "Position type cannot be null!")
    private PositionType positionType;

    private Integer officeId;  // Optional - може да е null за куриери

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters!")
    private String username;

    @NotBlank(message = "Email cannot be blank!")
    @Email(message = "Email should be valid!")
    private String email;

    // Пароли - ще се използват само при създаване
    private String password;
    private String confirmPassword;

    // Конвертиране към EmployeeDto
    public EmployeeDto toEmployeeDto() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setPositionType(this.positionType);
        employeeDto.setOfficeId(this.officeId);
        return employeeDto;
    }

    // Конвертиране към UserDto
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