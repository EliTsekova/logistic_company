package com.team14.logistic_company.services;
import com.team14.logistic_company.dtos.OfficeDto;

import java.util.List;

public interface IOfficeService {
    List<OfficeDto> findAll();
    OfficeDto findById(Integer id);
    OfficeDto create(OfficeDto officeDto);
    OfficeDto update(Integer id, OfficeDto officeDto);
    void delete(Integer id);
}