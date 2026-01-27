package com.team14.logistic_company.controllers.forms;
import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.entities.enums.PositionType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeForm {

    private Integer id;

    @NotNull(message = "Position type cannot be null!")
    private PositionType positionType;

    private Integer officeId;  // Optional

    // Конвертиране към EmployeeDto
    public EmployeeDto toEmployeeDto() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(this.id);
        employeeDto.setPositionType(this.positionType);
        employeeDto.setOfficeId(this.officeId);
        return employeeDto;
    }
}