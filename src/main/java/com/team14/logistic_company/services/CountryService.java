package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.CountryDto;
import com.team14.logistic_company.entities.Country;
import com.team14.logistic_company.repositories.CountryRepository;
import com.team14.logistic_company.services.exceptions.CountryNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CountryService implements ICountryService {

    private final CountryRepository countryRepository;
    @Override
    public List<CountryDto> findAll() {
        return countryRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CountryDto findById(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFound(id));
        return convertToDto(country);
    }
    @Override
    public CountryDto create(CountryDto countryDto) {
        Country country = convertToEntity(countryDto);
        Country saved = countryRepository.save(country);
        return convertToDto(saved);
    }
    @Override
    public CountryDto update(Integer id, CountryDto countryDto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + id));

        country.setName(countryDto.getName());

        Country updated = countryRepository.save(country);
        return convertToDto(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!countryRepository.existsById(id)) {
            throw new RuntimeException("Country not found with id: " + id);
        }
        countryRepository.deleteById(id);
    }

    // Converter methods
    private CountryDto convertToDto(Country country) {
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setCreatedOn(country.getCreatedOn());
        dto.setUpdatedOn(country.getUpdatedOn());
        return dto;
    }

    private Country convertToEntity(CountryDto dto) {
        Country country = new Country();
        country.setName(dto.getName());
        return country;
    }
}