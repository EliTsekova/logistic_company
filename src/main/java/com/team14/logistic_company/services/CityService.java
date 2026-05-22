package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.CityDto;
import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.entities.Country;
import com.team14.logistic_company.repositories.CityRepository;
import com.team14.logistic_company.repositories.CountryRepository;
import com.team14.logistic_company.services.exceptions.CityNotFound;
import com.team14.logistic_company.services.exceptions.CountryNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation responsible for city management operations.
 *
 * <p>
 * This service provides functionality for:
 * </p>
 * <ul>
 *     <li>Creating cities</li>
 *     <li>Updating existing cities</li>
 *     <li>Deleting cities</li>
 *     <li>Retrieving cities by id or country</li>
 *     <li>Finding or creating cities by name</li>
 *     <li>Converting entities to DTOs and vice versa</li>
 * </ul>
 *
 * <p>
 * The service communicates with repositories and validates
 * related country information before persisting data.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CityService implements ICityService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    /**
     * Retrieves all cities from the system.
     *
     * @return list of all cities as DTO objects
     */
    @Override
    public List<CityDto> findAll() {
        return cityRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds a city by its identifier.
     *
     * @param id city identifier
     * @return city DTO object
     * @throws CityNotFound if city does not exist
     */
    @Override
    public CityDto findById(Integer id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFound(id));

        return convertToDto(city);
    }

    /**
     * Retrieves all cities belonging to a specific country.
     *
     * @param countryId country identifier
     * @return list of cities in the specified country
     */
    @Override
    public List<CityDto> findByCountryId(Integer countryId) {
        return cityRepository.findByCountryId(countryId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new city in the system.
     *
     * @param cityDto DTO containing city information
     * @return created city as DTO object
     * @throws CountryNotFound if country does not exist
     */
    @Override
    public CityDto create(CityDto cityDto) {
        Country country = countryRepository.findById(cityDto.getCountryId())
                .orElseThrow(() -> new CountryNotFound(cityDto.getCountryId()));

        City city = convertToEntity(cityDto);
        city.setCountry(country);

        City saved = cityRepository.save(city);

        return convertToDto(saved);
    }

    /**
     * Finds an existing city by name and country or creates a new one.
     *
     * @param name city name
     * @param countryId country identifier
     * @return existing or newly created city DTO object
     */
    public CityDto findOrCreateByNameAndCountry(String name, Integer countryId) {
        String normalizedName = name.trim();

        return cityRepository.findAll()
                .stream()
                .filter(city ->
                        city.getName().equalsIgnoreCase(normalizedName)
                                && city.getCountry().getId().equals(countryId)
                )
                .findFirst()
                .map(this::convertToDto)
                .orElseGet(() -> {
                    CityDto dto = new CityDto();
                    dto.setName(normalizedName);
                    dto.setCountryId(countryId);
                    return create(dto);
                });
    }

    /**
     * Updates existing city information.
     *
     * @param id city identifier
     * @param cityDto DTO containing updated data
     * @return updated city as DTO object
     * @throws CityNotFound if city does not exist
     * @throws CountryNotFound if country does not exist
     */
    @Override
    public CityDto update(Integer id, CityDto cityDto) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFound(id));

        Country country = countryRepository.findById(cityDto.getCountryId())
                .orElseThrow(() -> new CountryNotFound(cityDto.getCountryId()));

        city.setName(cityDto.getName().trim());
        city.setCountry(country);

        City updated = cityRepository.save(city);

        return convertToDto(updated);
    }

    /**
     * Deletes a city from the system.
     *
     * @param id city identifier
     * @throws CityNotFound if city does not exist
     */
    @Override
    public void delete(Integer id) {
        if (!cityRepository.existsById(id)) {
            throw new CityNotFound(id);
        }

        cityRepository.deleteById(id);
    }

    /**
     * Converts City entity to CityDto object.
     *
     * @param city city entity
     * @return converted DTO object
     */
    public CityDto convertToDto(City city) {
        CityDto dto = new CityDto();

        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setCountryId(city.getCountry().getId());
        dto.setCreatedOn(city.getCreatedOn());
        dto.setUpdatedOn(city.getUpdatedOn());

        return dto;
    }

    /**
     * Converts CityDto object to City entity.
     *
     * @param dto city DTO object
     * @return converted city entity
     */
    private City convertToEntity(CityDto dto) {
        City city = new City();

        city.setName(dto.getName().trim());

        return city;
    }
}