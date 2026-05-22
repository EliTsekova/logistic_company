package com.team14.logistic_company.controllers.forms;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
/**
 * Form object used for creating new clients.
 * Extends {@link ClientForm} by adding validation rules
 * for password and password confirmation fields.
 */
@Getter
@Setter
public class CreateClientForm extends ClientForm {

    /**
     * Returns the client password.
     * The password field is required during client creation.
     *
     * @return client password
     */
    @NotBlank(message = "Password cannot be blank!")
    @Override
    public String getPassword() {
        return super.getPassword();
    }

    /**
     * Returns the password confirmation value.
     * The confirmation password field is required during client creation.
     *
     * @return confirmation password
     */
    @NotBlank(message = "Confirm password cannot be blank!")
    @Override
    public String getConfirmPassword() {
        return super.getConfirmPassword();
    }
}