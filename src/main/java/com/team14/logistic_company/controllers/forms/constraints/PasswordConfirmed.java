package com.team14.logistic_company.controllers.forms.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation used to verify that two password fields match.
 * <p>
 * Typically applied to form classes containing password and
 * confirmation password fields during user registration or password update.
 * </p>
 *
 * The validator compares the values of the fields specified by
 * {@code passwordField()} and {@code confirmPasswordField()}.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordConfirmedValidator.class)
public @interface PasswordConfirmed {

    /**
     * Validation error message displayed when the passwords do not match.
     *
     * @return default validation message
     */
    String message() default "Passwords must match!";

    /**
     * Validation groups used for grouping constraints.
     *
     * @return validation groups
     */
    Class<?>[] groups() default {};

    /**
     * Additional payload data associated with the constraint.
     *
     * @return payload classes
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Name of the password field to validate.
     *
     * @return password field name
     */
    String passwordField();

    /**
     * Name of the confirmation password field.
     *
     * @return confirmation password field name
     */
    String confirmPasswordField();
}