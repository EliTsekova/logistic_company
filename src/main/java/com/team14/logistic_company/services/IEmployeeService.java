package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.entities.enums.PositionType;

import java.util.List;

/**
 * Service interface for managing Employee operations.
 *
 * <p>
 * Defines the contract for CRUD operations and queries
 * related to employees in the system.
 * </p>
 */
public interface IEmployeeService {

    /**
     * Retrieves all employees from the system.
     *
     * @return list of EmployeeDto objects
     */
    List<EmployeeDto> findAll();

    /**
     * Finds an employee by its identifier.
     *
     * @param id employee identifier
     * @return EmployeeDto object
     */
    EmployeeDto findById(Integer id);

    /**
     * Finds an employee by related user identifier.
     *
     * @param userId user identifier
     * @return EmployeeDto object
     */
    EmployeeDto findByUserId(Integer userId);

    /**
     * Finds employees by position type.
     *
     * @param positionType employee position type
     * @return list of EmployeeDto objects
     */
    List<EmployeeDto> findByPositionType(PositionType positionType);

    /**
     * Finds employees by office identifier.
     *
     * @param officeId office identifier
     * @return list of EmployeeDto objects
     */
    List<EmployeeDto> findByOfficeId(Integer officeId);

    /**
     * Creates a new employee in the system.
     *
     * @param employeeDto data transfer object containing employee data
     * @return created EmployeeDto object
     */
    EmployeeDto create(EmployeeDto employeeDto);

    /**
     * Updates an existing employee.
     *
     * @param id employee identifier
     * @param employeeDto updated employee data
     * @return updated EmployeeDto object
     */
    EmployeeDto update(Integer id, EmployeeDto employeeDto);

    /**
     * Deletes an employee by its identifier.
     *
     * @param id employee identifier
     */
    void delete(Integer id);

    /**
     * Finds an employee by username.
     *
     * @param username username of the user
     * @return EmployeeDto object
     */
    EmployeeDto findByUsername(String username);
}