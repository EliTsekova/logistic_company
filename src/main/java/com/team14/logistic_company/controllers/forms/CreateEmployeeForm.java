package com.team14.logistic_company.controllers.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEmployeeForm extends EmployeeForm {

    @NotBlank(message = "Password cannot be blank!")
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @NotBlank(message = "Confirm password cannot be blank!")
    @Override
    public String getConfirmPassword() {
        return super.getConfirmPassword();
    }
}