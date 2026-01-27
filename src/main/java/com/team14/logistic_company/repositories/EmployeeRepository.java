package com.team14.logistic_company.repositories;
import com.team14.logistic_company.entities.Employee;
import com.team14.logistic_company.entities.enums.PositionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // Намиране по User ID
    Optional<Employee> findByUserId(Integer userId);

    // Намиране по позиция
    List<Employee> findByPositionType(PositionType positionType);

    // Намиране по офис
    List<Employee> findByOfficeId(Integer officeId);

    // Проверка дали съществува по User ID
    boolean existsByUserId(Integer userId);
}