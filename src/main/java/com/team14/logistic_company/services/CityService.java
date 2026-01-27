package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.CityDto;
import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.entities.Country;
import com.team14.logistic_company.services.exceptions.CityNotFound;
import com.team14.logistic_company.services.exceptions.CountryNotFound;
import com.team14.logistic_company.repositories.CityRepository;
import com.team14.logistic_company.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CityService implements ICityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Override
    public List<CityDto> findAll() {
        return cityRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto findById(Integer id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found with id: " + id));
        return convertToDto(city);
    }

    @Override
    public List<CityDto> findByCountryId(Integer countryId) {
        return cityRepository.findByCountryId(countryId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CityDto create(CityDto cityDto) {
        Country country = countryRepository.findById(cityDto.getCountryId())
                .orElseThrow(() -> new CountryNotFound(cityDto.getCountryId()));

        City city = convertToEntity(cityDto);
        city.setCountry(country);

        City saved = cityRepository.save(city);
        return convertToDto(saved);
    }

    @Override
    public CityDto update(Integer id, CityDto cityDto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFound(id));

        Country country = countryRepository.findById(cityDto.getCountryId())
                .orElseThrow(() -> new CountryNotFound(cityDto.getCountryId()));

        city.setName(cityDto.getName());
        city.setCountry(country);

        City updated = cityRepository.save(city);
        return convertToDto(updated);
    }

    @Override
    public void delete(Integer id) {
        if (!cityRepository.existsById(id)) {
            throw new RuntimeException("City not found with id: " + id);
        }
        cityRepository.deleteById(id);
    }

    // Converter methods
    private CityDto convertToDto(City city) {
        CityDto dto = new CityDto();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setCountryId(city.getCountry().getId());
        dto.setCreatedOn(city.getCreatedOn());
        dto.setUpdatedOn(city.getUpdatedOn());
        return dto;
    }

    private City convertToEntity(CityDto dto) {
        City city = new City();
        city.setName(dto.getName());
        return city;
    }
}