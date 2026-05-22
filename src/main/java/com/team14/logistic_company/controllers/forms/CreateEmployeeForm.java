package com.team14.logistic_company.controllers.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
/**
 * Form object used for creating new employees.
 * Extends {@link EmployeeForm} by adding validation rules
 * for password and password confirmation fields.
 */
@Getter
@Setter
public class CreateEmployeeForm extends EmployeeForm {

    /**
     * Returns the employee password.
     * The password field is required during employee creation.
     *
     * @return employee password
     */
    @NotBlank(message = "Password cannot be blank!")
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    /**
     * Returns the password confirmation value.
     * The confirmation password field is required during employee creation.
     *
     * @return confirmation password
     */
    @NotBlank(message = "Confirm password cannot be blank!")
    @Override
    public String getConfirmPassword() {
        return super.getConfirmPassword();
    }
}