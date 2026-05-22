package com.team14.logistic_company.controllers.forms.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

/**
 * Validator implementation for the {@link PasswordConfirmed} annotation.
 * <p>
 * This validator checks whether the values of two password-related fields
 * in a form object are equal.
 * </p>
 *
 * The field names are provided through the annotation parameters
 * {@code passwordField()} and {@code confirmPasswordField()}.
 */
public class PasswordConfirmedValidator
        implements ConstraintValidator<PasswordConfirmed, Object> {

    /**
     * Name of the password field.
     */
    private String passwordFieldName;

    /**
     * Name of the confirmation password field.
     */
    private String confirmPasswordFieldName;

    /**
     * Validation error message.
     */
    private String message;

    /**
     * Initializes the validator with annotation parameters.
     *
     * @param constraintAnnotation annotation containing validation configuration
     */
    @Override
    public void initialize(PasswordConfirmed constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.passwordField();
        this.confirmPasswordFieldName = constraintAnnotation.confirmPasswordField();
        this.message = constraintAnnotation.message();
    }

    /**
     * Validates whether the password and confirmation password match.
     *
     * @param value   object being validated
     * @param context validation context used for building custom violations
     * @return {@code true} if both password fields match;
     *         otherwise {@code false}
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String password = getFieldValue(value, passwordFieldName);
            String confirmPassword = getFieldValue(value, confirmPasswordFieldName);

            if (password == null && confirmPassword == null) {
                return true;
            }

            boolean isValid = password != null && password.equals(confirmPassword);

            if (!isValid) {
                context.disableDefaultConstraintViolation();

                context.buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(confirmPasswordFieldName)
                        .addConstraintViolation();
            }

            return isValid;

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retrieves the value of a field using reflection.
     *
     * @param object    target object containing the field
     * @param fieldName name of the field
     * @return field value converted to string, or {@code null} if empty
     * @throws Exception if the field cannot be accessed
     */
    private String getFieldValue(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);

        field.setAccessible(true);

        Object value = field.get(object);

        return value != null ? value.toString() : null;
    }
}