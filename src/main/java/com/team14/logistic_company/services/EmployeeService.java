package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.entities.Employee;
import com.team14.logistic_company.entities.Office;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.services.exceptions.EmployeeNotFound;
import com.team14.logistic_company.services.exceptions.OfficeNotFound;
import com.team14.logistic_company.repositories.EmployeeRepository;
import com.team14.logistic_company.repositories.OfficeRepository;
import com.team14.logistic_company.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation responsible for employee management operations.
 *
 * <p>
 * This service provides functionality for:
 * </p>
 * <ul>
 *     <li>Retrieving all employees</li>
 *     <li>Finding employees by id, user id, username, position type, or office</li>
 *     <li>Creating new employee profiles</li>
 *     <li>Updating employee information</li>
 *     <li>Deleting employees</li>
 *     <li>Converting Employee entities to DTO objects</li>
 * </ul>
 *
 * <p>
 * The service communicates with employee, user, and office repositories.
 * It validates related user and office data before creating or updating employees.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final OfficeRepository officeRepository;

    /**
     * Retrieves all employees from the system.
     *
     * @return list of all employees as DTO objects
     */
    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds an employee by its identifier.
     *
     * @param id employee identifier
     * @return employee DTO object
     * @throws EmployeeNotFound if employee does not exist
     */
    @Override
    public EmployeeDto findById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFound(id));
        return convertToDto(employee);
    }

    /**
     * Finds an employee profile by related user identifier.
     *
     * @param userId user identifier
     * @return employee DTO object connected to the given user
     * @throws EmployeeNotFound if employee for the given user does not exist
     */
    @Override
    public EmployeeDto findByUserId(Integer userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found for user id: " + userId));
        return convertToDto(employee);
    }

    /**
     * Retrieves all employees with a specific position type.
     *
     * @param positionType employee position type
     * @return list of employees with the specified position type
     */
    @Override
    public List<EmployeeDto> findByPositionType(PositionType positionType) {
        return employeeRepository.findByPositionType(positionType)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all employees assigned to a specific office.
     *
     * @param officeId office identifier
     * @return list of employees assigned to the specified office
     */
    @Override
    public List<EmployeeDto> findByOfficeId(Integer officeId) {
        return employeeRepository.findByOfficeId(officeId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new employee profile for an existing user.
     *
     * <p>
     * If office id is provided, the employee is assigned to the selected office.
     * </p>
     *
     * @param employeeDto DTO containing employee information
     * @return created employee as DTO object
     * @throws RuntimeException if user does not exist
     * @throws OfficeNotFound if selected office does not exist
     */
    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        User user = userRepository.findById(employeeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + employeeDto.getUserId()));

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setPositionType(employeeDto.getPositionType());

        if (employeeDto.getOfficeId() != null) {
            Office office = officeRepository.findById(employeeDto.getOfficeId())
                    .orElseThrow(() -> new OfficeNotFound(employeeDto.getOfficeId()));
            employee.setOffice(office);
        }

        Employee saved = employeeRepository.save(employee);
        return convertToDto(saved);
    }

    /**
     * Updates existing employee information.
     *
     * <p>
     * Updates the employee position type and office assignment.
     * If office id is null, the employee is removed from any office.
     * </p>
     *
     * @param id employee identifier
     * @param employeeDto DTO containing updated employee data
     * @return updated employee as DTO object
     * @throws EmployeeNotFound if employee does not exist
     * @throws OfficeNotFound if selected office does not exist
     */
    @Override
    public EmployeeDto update(Integer id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFound(id));

        employee.setPositionType(employeeDto.getPositionType());

        if (employeeDto.getOfficeId() != null) {
            Office office = officeRepository.findById(employeeDto.getOfficeId())
                    .orElseThrow(() -> new OfficeNotFound(employeeDto.getOfficeId()));
            employee.setOffice(office);
        } else {
            employee.setOffice(null);
        }

        Employee updated = employeeRepository.save(employee);
        return convertToDto(updated);
    }

    /**
     * Deletes an employee from the system.
     *
     * @param id employee identifier
     * @throws EmployeeNotFound if employee does not exist
     */
    @Override
    public void delete(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFound(id);
        }
        employeeRepository.deleteById(id);
    }

    /**
     * Converts Employee entity to EmployeeDto object.
     *
     * <p>
     * The method also copies related user information,
     * such as name, email and username. If the employee is assigned
     * to an office, the office title is also copied.
     * </p>
     *
     * @param employee employee entity
     * @return converted DTO object containing employee, user and office data
     */
    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setPositionType(employee.getPositionType());
        dto.setUserId(employee.getUser().getId());
        dto.setOfficeId(employee.getOffice() != null ? employee.getOffice().getId() : null);
        dto.setCreatedOn(employee.getCreatedOn());
        dto.setUpdatedOn(employee.getUpdatedOn());

        User user = employee.getUser();
        dto.setUserFirstName(user.getFirstName());
        dto.setUserLastName(user.getLastName());
        dto.setUserFullName(user.getFirstName() + " " + user.getLastName());
        dto.setUserEmail(user.getEmail());
        dto.setUserUsername(user.getUsername());

        if (employee.getOffice() != null) {
            dto.setOfficeTitle(employee.getOffice().getTitle());
        }

        return dto;
    }

    /**
     * Finds an employee profile by username.
     *
     * <p>
     * The method first finds the user by username and then searches
     * for the employee profile connected to that user.
     * </p>
     *
     * @param username username connected to the employee profile
     * @return employee DTO object connected to the given username
     * @throws RuntimeException if user does not exist
     * @throws EmployeeNotFound if employee profile for the username does not exist
     */
    @Override
    public EmployeeDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        Employee employee = employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EmployeeNotFound("Employee not found for username: " + username));

        return convertToDto(employee);
    }
}