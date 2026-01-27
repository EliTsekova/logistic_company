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

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final OfficeRepository officeRepository;

    @Override
    public List<EmployeeDto> findAll() {
        return employeeRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto findById(Integer id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFound(id));
        return convertToDto(employee);
    }

    @Override
    public EmployeeDto findByUserId(Integer userId) {
        Employee employee = employeeRepository.findByUserId(userId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found for user id: " + userId));
        return convertToDto(employee);
    }

    @Override
    public List<EmployeeDto> findByPositionType(PositionType positionType) {
        return employeeRepository.findByPositionType(positionType)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EmployeeDto> findByOfficeId(Integer officeId) {
        return employeeRepository.findByOfficeId(officeId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto create(EmployeeDto employeeDto) {
        // Намери User
        User user = userRepository.findById(employeeDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + employeeDto.getUserId()));

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setPositionType(employeeDto.getPositionType());

        // Ако има officeId, намери офиса
        if (employeeDto.getOfficeId() != null) {
            Office office = officeRepository.findById(employeeDto.getOfficeId())
                    .orElseThrow(() -> new OfficeNotFound(employeeDto.getOfficeId()));
            employee.setOffice(office);
        }

        Employee saved = employeeRepository.save(employee);
        return convertToDto(saved);
    }

    @Override
    public EmployeeDto update(Integer id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFound(id));

        employee.setPositionType(employeeDto.getPositionType());

        // Обнови офиса ако има
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

    @Override
    public void delete(Integer id) {
        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFound(id);
        }
        employeeRepository.deleteById(id);
    }

    // Converter methods
    private EmployeeDto convertToDto(Employee employee) {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(employee.getId());
        dto.setPositionType(employee.getPositionType());
        dto.setUserId(employee.getUser().getId());
        dto.setOfficeId(employee.getOffice() != null ? employee.getOffice().getId() : null);
        dto.setCreatedOn(employee.getCreatedOn());
        dto.setUpdatedOn(employee.getUpdatedOn());

        // User информация
        User user = employee.getUser();
        dto.setUserFirstName(user.getFirstName());
        dto.setUserLastName(user.getLastName());
        dto.setUserFullName(user.getFirstName() + " " + user.getLastName());
        dto.setUserEmail(user.getEmail());
        dto.setUserUsername(user.getUsername());

        // Office информация
        if (employee.getOffice() != null) {
            dto.setOfficeTitle(employee.getOffice().getTitle());
        }

        return dto;
    }
}