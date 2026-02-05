package com.team14.logistic_company.database_tests;


import com.team14.logistic_company.entities.Client;
import com.team14.logistic_company.entities.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTest {

    private User user;

    private List<String> validate(Client client) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(client)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @BeforeEach
    void init() {
        user = new User(); // за Bean Validation теста е достатъчно да е non-null
    }

    private Client buildClient(String phoneNumber) {
        Client client = new Client();
        client.setUser(user);              // няма @NotNull, но го задаваме
        client.setPhoneNumber(phoneNumber);
        return client;
    }

    @Test
    void whenDataIsValid() {
        Client client = buildClient("0888888888");

        List<String> messages = validate(client);

        assertEquals(0, messages.size());
    }

    @Test
    void whenPhoneNumberIsTooShort() {
        Client client = buildClient("08");

        List<String> messages = validate(client);

        // "08" нарушава само @Size(min=10,max=10)
        assertEquals(1, messages.size());
        assertTrue(messages.contains("The phone number has to be exactly 10 characters!"));
    }

    @Test
    void whenPhoneNumberIsBlank() {
        Client client = buildClient("");

        List<String> messages = validate(client);

        // "" нарушава @NotBlank и @Size(exactly 10)
        assertEquals(2, messages.size());
        assertTrue(messages.contains("Phone number cannot be blank!"));
        assertTrue(messages.contains("The phone number has to be exactly 10 characters!"));
    }
}
