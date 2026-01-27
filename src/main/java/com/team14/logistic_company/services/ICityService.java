package com.team14.logistic_company.services;
import com.team14.logistic_company.dtos.CityDto;

import java.util.List;

public interface ICityService {
    List<CityDto> findAll();
    CityDto findById(Integer id);
    List<CityDto> findByCountryId(Integer countryId);
    CityDto create(CityDto cityDto);
    CityDto update(Integer id, CityDto cityDto);
    void delete(Integer id);
}