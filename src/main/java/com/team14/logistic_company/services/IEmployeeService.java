package com.team14.logistic_company.services;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.entities.enums.PositionType;

import java.util.List;

public interface IEmployeeService {
    List<EmployeeDto> findAll();
    EmployeeDto findById(Integer id);
    EmployeeDto findByUserId(Integer userId);
    List<EmployeeDto> findByPositionType(PositionType positionType);
    List<EmployeeDto> findByOfficeId(Integer officeId);
    EmployeeDto create(EmployeeDto employeeDto);
    EmployeeDto update(Integer id, EmployeeDto employeeDto);
    void delete(Integer id);
}