package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
