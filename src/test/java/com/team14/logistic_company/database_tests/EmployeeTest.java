package com.team14.logistic_company.database_tests;

import com.team14.logistic_company.entities.Employee;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.PositionType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeTest {

    private List<String> validate(Employee employee) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(employee)
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    private Employee buildEmployee(PositionType positionType) {
        Employee employee = new Employee();
        employee.setUser(new User());          // за Bean Validation е достатъчно
        employee.setPositionType(positionType);
        employee.setOffice(null);              // куриерът може да няма офис
        return employee;
    }

    @Test
    void whenDataIsValid_deliveryman() {
        Employee employee = buildEmployee(PositionType.DELIVERYMAN);

        List<String> messages = validate(employee);

        assertEquals(0, messages.size());
    }

    @Test
    void whenDataIsValid_coordinator() {
        Employee employee = buildEmployee(PositionType.COORDINATOR);

        List<String> messages = validate(employee);

        assertEquals(0, messages.size());
    }

    @Test
    void whenPositionTypeIsNull_validationDoesNotFail() {
        Employee employee = new Employee();
        employee.setUser(new User());

        List<String> messages = validate(employee);

        // няма @NotNull → няма Bean Validation грешка
        assertEquals(0, messages.size());
    }

    @Test
    void whenUserIsNull_validationDoesNotFail() {
        Employee employee = new Employee();
        employee.setPositionType(PositionType.DELIVERYMAN);

        List<String> messages = validate(employee);

        assertEquals(0, messages.size());
    }
}
