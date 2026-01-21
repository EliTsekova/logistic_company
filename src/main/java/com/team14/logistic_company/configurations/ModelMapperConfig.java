package com.team14.logistic_company.configurations;

import com.team14.logistic_company.dtos.EmployeeDto;
import com.team14.logistic_company.entities.Employee;
//import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/// can be used in the project
@Configuration
public class ModelMapperConfig {
    /*@Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper
                .typeMap(Employee.class, EmployeeDto.class)
                .addMappings(
                        mapping -> {
                            mapping.map(src -> src.getUser().getFullName(), EmployeeDto::setUserFullName);
                            mapping.map(src -> src.getUser().getEmail(), EmployeeDto::setUserEmail);
                            mapping.map(src -> src.getOffice().getTitle(), EmployeeDto::setOfficeTitle);
                            mapping.map(src -> src.getUser().getId(), EmployeeDto::setUserId);
                        }
                );

        return modelMapper;
    }*/
}