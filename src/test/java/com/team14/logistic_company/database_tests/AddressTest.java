package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.City;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AddressTest {

    private List<String> validate(Address address) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(address)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private Address buildAddress(String street, String postalCode) {
        Address address = new Address();
        address.setStreet(street);
        address.setPostalCode(postalCode);

        // City няма @NotNull/@NotBlank в твоя клас, така че Bean Validation няма да го валидира.
        // Все пак го задаваме, за да е "реалистичен" обект (и да не те удари по-късно при persistence).
        address.setCity(new City());

        return address;
    }

    @Test
    void whenDataIsValid() {
        Address address = buildAddress("Street 1", "BG1234");

        List<String> messages = validate(address);

        assertEquals(0, messages.size());
    }

    @Test
    void whenStreetIsEmpty() {
        Address address = buildAddress("", "BG1234");

        List<String> messages = validate(address);

        // При празен стринг се очакват 2 нарушения: @NotBlank и @Size(min=5)
        assertEquals(2, messages.size());
        assertTrue(messages.contains("The street name cannot be blank!"));
        assertTrue(messages.contains("The street name has to be between 5 and 20 characters!"));
    }

    @Test
    void whenStreetIsLessThanFiveCharacters() {
        Address address = buildAddress("str", "BG1234");

        List<String> messages = validate(address);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The street name has to be between 5 and 20 characters!"));
    }

    @Test
    void whenPostalCodeIsBlank() {
        Address address = buildAddress("Street 1", "");

        List<String> messages = validate(address);

        assertEquals(1, messages.size());
        assertTrue(messages.contains("The postal code cannot be blank!"));
    }
}
