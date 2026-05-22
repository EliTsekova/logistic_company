package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.entities.Employee;
import com.team14.logistic_company.entities.Office;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.repositories.EmployeeRepository;
import com.team14.logistic_company.repositories.OfficeRepository;
import com.team14.logistic_company.repositories.UserRepository;
import com.team14.logistic_company.services.EmployeeService;
import com.team14.logistic_company.services.exceptions.EmployeeNotFound;
import com.team14.logistic_company.services.exceptions.OfficeNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link EmployeeService} class.
 *
 * These tests verify employee service operations,
 * DTO conversion and repository interactions.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;
    private User user;
    private Office office;

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        user = mock(User.class);
        lenient().when(user.getId()).thenReturn(1);
        lenient().when(user.getFirstName()).thenReturn("Ivan");
        lenient().when(user.getLastName()).thenReturn("Ivanov");
        lenient().when(user.getEmail()).thenReturn("ivan@test.com");
        lenient().when(user.getUsername()).thenReturn("ivan123");

        office = mock(Office.class);
        lenient().when(office.getId()).thenReturn(1);
        lenient().when(office.getTitle()).thenReturn("Office Sofia");

        employee = new Employee();
        employee.setUser(user);
        employee.setOffice(office);
        employee.setPositionType(PositionType.DELIVERYMAN);

        employeeDto = new EmployeeDto();
        employeeDto.setUserId(1);
        employeeDto.setOfficeId(1);
        employeeDto.setPositionType(PositionType.DELIVERYMAN);
    }

    /**
     * Tests that all employees are returned successfully.
     */
    @Test
    void shouldFindAllEmployees() {

        when(employeeRepository.findAll())
                .thenReturn(List.of(employee));

        List<EmployeeDto> result =
                employeeService.findAll();

        assertEquals(1, result.size());
        assertEquals(PositionType.DELIVERYMAN, result.get(0).getPositionType());
        assertEquals("Ivan Ivanov", result.get(0).getUserFullName());

        verify(employeeRepository)
                .findAll();
    }

    /**
     * Tests that an employee is found successfully by ID.
     */
    @Test
    void shouldFindEmployeeById() {

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        EmployeeDto result =
                employeeService.findById(1);

        assertNotNull(result);
        assertEquals(PositionType.DELIVERYMAN, result.getPositionType());
        assertEquals("ivan123", result.getUserUsername());

        verify(employeeRepository)
                .findById(1);
    }

    /**
     * Tests that EmployeeNotFound is thrown
     * when an employee with the given ID does not exist.
     */
    @Test
    void shouldThrowWhenEmployeeNotFoundById() {

        when(employeeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFound.class,
                () -> employeeService.findById(1)
        );
    }

    /**
     * Tests that an employee is found successfully by user ID.
     */
    @Test
    void shouldFindEmployeeByUserId() {

        when(employeeRepository.findByUserId(1))
                .thenReturn(Optional.of(employee));

        EmployeeDto result =
                employeeService.findByUserId(1);

        assertNotNull(result);
        assertEquals("Ivan", result.getUserFirstName());

        verify(employeeRepository)
                .findByUserId(1);
    }

    /**
     * Tests that EmployeeNotFound is thrown
     * when no employee exists for the given user ID.
     */
    @Test
    void shouldThrowWhenEmployeeNotFoundByUserId() {

        when(employeeRepository.findByUserId(1))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFound.class,
                () -> employeeService.findByUserId(1)
        );
    }

    /**
     * Tests that employees are found by position type.
     */
    @Test
    void shouldFindEmployeesByPositionType() {

        when(employeeRepository.findByPositionType(PositionType.DELIVERYMAN))
                .thenReturn(List.of(employee));

        List<EmployeeDto> result =
                employeeService.findByPositionType(PositionType.DELIVERYMAN);

        assertEquals(1, result.size());
        assertEquals(PositionType.DELIVERYMAN, result.get(0).getPositionType());

        verify(employeeRepository)
                .findByPositionType(PositionType.DELIVERYMAN);
    }

    /**
     * Tests that employees are found by office ID.
     */
    @Test
    void shouldFindEmployeesByOfficeId() {

        when(employeeRepository.findByOfficeId(1))
                .thenReturn(List.of(employee));

        List<EmployeeDto> result =
                employeeService.findByOfficeId(1);

        assertEquals(1, result.size());
        assertEquals("Office Sofia", result.get(0).getOfficeTitle());

        verify(employeeRepository)
                .findByOfficeId(1);
    }

    /**
     * Tests that a new employee is created successfully
     * when both user and office exist.
     */
    @Test
    void shouldCreateEmployeeWithOffice() {

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(officeRepository.findById(1))
                .thenReturn(Optional.of(office));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeDto result =
                employeeService.create(employeeDto);

        assertNotNull(result);
        assertEquals(PositionType.DELIVERYMAN, result.getPositionType());
        assertEquals("Office Sofia", result.getOfficeTitle());

        verify(userRepository)
                .findById(1);

        verify(officeRepository)
                .findById(1);

        verify(employeeRepository)
                .save(any(Employee.class));
    }

    /**
     * Tests that a new employee can be created
     * without an office.
     */
    @Test
    void shouldCreateEmployeeWithoutOffice() {

        employeeDto.setOfficeId(null);
        employee.setOffice(null);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeDto result =
                employeeService.create(employeeDto);

        assertNotNull(result);
        assertNull(result.getOfficeId());

        verify(officeRepository, never())
                .findById(anyInt());

        verify(employeeRepository)
                .save(any(Employee.class));
    }

    /**
     * Tests that RuntimeException is thrown
     * when creating an employee with non-existing user.
     */
    @Test
    void shouldThrowWhenCreatingEmployeeWithInvalidUser() {

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> employeeService.create(employeeDto)
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    /**
     * Tests that OfficeNotFound is thrown
     * when creating an employee with invalid office ID.
     */
    @Test
    void shouldThrowWhenCreatingEmployeeWithInvalidOffice() {

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(officeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                OfficeNotFound.class,
                () -> employeeService.create(employeeDto)
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    /**
     * Tests that an existing employee is updated successfully.
     */
    @Test
    void shouldUpdateEmployeeWithOffice() {

        employeeDto.setPositionType(PositionType.COORDINATOR);

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        when(officeRepository.findById(1))
                .thenReturn(Optional.of(office));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeDto result =
                employeeService.update(1, employeeDto);

        assertNotNull(result);
        assertEquals(PositionType.COORDINATOR, result.getPositionType());

        verify(employeeRepository)
                .save(employee);
    }

    /**
     * Tests that an existing employee can be updated
     * by removing the office relation.
     */
    @Test
    void shouldUpdateEmployeeWithoutOffice() {

        employeeDto.setOfficeId(null);
        employee.setOffice(null);

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(employee);

        EmployeeDto result =
                employeeService.update(1, employeeDto);

        assertNotNull(result);
        assertNull(result.getOfficeId());

        verify(officeRepository, never())
                .findById(anyInt());

        verify(employeeRepository)
                .save(employee);
    }

    /**
     * Tests that EmployeeNotFound is thrown
     * when updating a non-existing employee.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidEmployee() {

        when(employeeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFound.class,
                () -> employeeService.update(1, employeeDto)
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    /**
     * Tests that OfficeNotFound is thrown
     * when updating with an invalid office ID.
     */
    @Test
    void shouldThrowWhenUpdatingEmployeeWithInvalidOffice() {

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        when(officeRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                OfficeNotFound.class,
                () -> employeeService.update(1, employeeDto)
        );

        verify(employeeRepository, never())
                .save(any(Employee.class));
    }

    /**
     * Tests that an employee is deleted successfully.
     */
    @Test
    void shouldDeleteEmployee() {

        when(employeeRepository.existsById(1))
                .thenReturn(true);

        employeeService.delete(1);

        verify(employeeRepository)
                .deleteById(1);
    }

    /**
     * Tests that EmployeeNotFound is thrown
     * when deleting a non-existing employee.
     */
    @Test
    void shouldThrowWhenDeletingInvalidEmployee() {

        when(employeeRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                EmployeeNotFound.class,
                () -> employeeService.delete(1)
        );

        verify(employeeRepository, never())
                .deleteById(1);
    }

    /**
     * Tests that an employee is found successfully by username.
     */
    @Test
    void shouldFindEmployeeByUsername() {

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.of(user));

        when(employeeRepository.findByUserId(1))
                .thenReturn(Optional.of(employee));

        EmployeeDto result =
                employeeService.findByUsername("ivan123");

        assertNotNull(result);
        assertEquals("ivan123", result.getUserUsername());

        verify(userRepository)
                .findByUsername("ivan123");

        verify(employeeRepository)
                .findByUserId(1);
    }

    /**
     * Tests that RuntimeException is thrown
     * when no user exists for the given username.
     */
    @Test
    void shouldThrowWhenFindingByInvalidUsername() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> employeeService.findByUsername("missing")
        );
    }

    /**
     * Tests that EmployeeNotFound is thrown
     * when user exists but employee profile does not.
     */
    @Test
    void shouldThrowWhenEmployeeProfileForUsernameNotFound() {

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.of(user));

        when(employeeRepository.findByUserId(1))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFound.class,
                () -> employeeService.findByUsername("ivan123")
        );
    }
}