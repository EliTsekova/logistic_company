package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Employee;
import com.team14.logistic_company.entities.enums.PositionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface used for employee database operations.
 * Provides methods for searching and validating employee entities.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * Finds an employee by associated user identifier.
     *
     * @param userId the user identifier
     * @return optional containing the employee if found
     */
    Optional<Employee> findByUserId(Integer userId);

    /**
     * Returns all employees with the specified position type.
     *
     * @param positionType the employee position
     * @return list of employees
     */
    List<Employee> findByPositionType(PositionType positionType);

    /**
     * Returns all employees working in a specific office.
     *
     * @param officeId the office identifier
     * @return list of employees
     */
    List<Employee> findByOfficeId(Integer officeId);

    /**
     * Checks whether an employee with the given user identifier exists.
     *
     * @param userId the user identifier
     * @return true if employee exists
     */
    boolean existsByUserId(Integer userId);
}