package com.team14.logistic_company.controllers.forms.constraints;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class PasswordConfirmedValidator  // ← class (обикновен клас!)
        implements ConstraintValidator<PasswordConfirmed, Object> {

    private String passwordFieldName;
    private String confirmPasswordFieldName;
    private String message;

    @Override
    public void initialize(PasswordConfirmed constraintAnnotation) {
        this.passwordFieldName = constraintAnnotation.passwordField();
        this.confirmPasswordFieldName = constraintAnnotation.confirmPasswordField();
        this.message = constraintAnnotation.message();
    }

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

    private String getFieldValue(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(object);
        return value != null ? value.toString() : null;
    }
}