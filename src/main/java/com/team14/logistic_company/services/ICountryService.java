package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.CountryDto;

import java.util.List;

public interface ICountryService {
    List<CountryDto> findAll();
    CountryDto findById(Integer id);
    CountryDto create(CountryDto countryDto);
    CountryDto update(Integer id, CountryDto countryDto);
    void delete(Integer id);
}