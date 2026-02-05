package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.Office;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class OfficeTest {

    private List<String> validate(Office office) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(office)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private Office buildOffice(String title) {
        Office office = new Office();
        office.setTitle(title);
        office.setAddress(new Address());
        return office;
    }

    @Test
    void whenDataIsValid() {
        Office office = buildOffice("Central Office");

        List<String> messages = validate(office);

        assertEquals(0, messages.size());
    }

    @Test
    void whenTitleIsBlank() {
        Office office = buildOffice("");

        List<String> messages = validate(office);

        // "" нарушава @NotBlank и @Size(min=5)
        assertEquals(2, messages.size());
        assertTrue(messages.contains("Title cannot be blank!"));
        assertTrue(messages.contains("Title has to be between 5 and 20 characters!"));
    }

    @Test
    void whenTitleIsTooShort() {
        Office office = buildOffice("Off");

        List<String> messages = validate(office);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Title has to be between 5 and 20 characters!"));
    }

    @Test
    void whenTitleIsTooLong() {
        Office office = buildOffice("Very Very Long Office Title");

        List<String> messages = validate(office);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("Title has to be between 5 and 20 characters!"));
    }
}
