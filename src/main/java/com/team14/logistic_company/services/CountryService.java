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

/**
 * Service implementation responsible for country management operations.
 *
 * <p>
 * This service provides functionality for:
 * </p>
 * <ul>
 *     <li>Creating countries</li>
 *     <li>Updating existing countries</li>
 *     <li>Deleting countries</li>
 *     <li>Retrieving countries by id</li>
 *     <li>Finding or creating countries by name</li>
 *     <li>Converting entities to DTOs and vice versa</li>
 * </ul>
 *
 * <p>
 * The service communicates with the country repository
 * to manage country-related data in the system.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CountryService implements ICountryService {

    private final CountryRepository countryRepository;

    /**
     * Retrieves all countries from the system.
     *
     * @return list of all countries as DTO objects
     */
    @Override
    public List<CountryDto> findAll() {
        return countryRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds a country by its identifier.
     *
     * @param id country identifier
     * @return country DTO object
     * @throws CountryNotFound if country does not exist
     */
    @Override
    public CountryDto findById(Integer id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFound(id));

        return convertToDto(country);
    }

    /**
     * Creates a new country in the system.
     *
     * @param countryDto DTO containing country information
     * @return created country as DTO object
     */
    @Override
    public CountryDto create(CountryDto countryDto) {
        Country country = convertToEntity(countryDto);
        Country saved = countryRepository.save(country);

        return convertToDto(saved);
    }

    /**
     * Finds an existing country by name or creates a new one.
     *
     * @param name country name
     * @return existing or newly created country DTO object
     */
    public CountryDto findOrCreateByName(String name) {
        String normalizedName = name.trim();

        return countryRepository.findAll()
                .stream()
                .filter(country -> country.getName().equalsIgnoreCase(normalizedName))
                .findFirst()
                .map(this::convertToDto)
                .orElseGet(() -> {
                    CountryDto dto = new CountryDto();
                    dto.setName(normalizedName);
                    return create(dto);
                });
    }

    /**
     * Updates existing country information.
     *
     * @param id country identifier
     * @param countryDto DTO containing updated data
     * @return updated country as DTO object
     * @throws CountryNotFound if country does not exist
     */
    @Override
    public CountryDto update(Integer id, CountryDto countryDto) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new CountryNotFound(id));

        country.setName(countryDto.getName());

        Country updated = countryRepository.save(country);

        return convertToDto(updated);
    }

    /**
     * Deletes a country from the system.
     *
     * @param id country identifier
     * @throws CountryNotFound if country does not exist
     */
    @Override
    public void delete(Integer id) {
        if (!countryRepository.existsById(id)) {
            throw new CountryNotFound(id);
        }

        countryRepository.deleteById(id);
    }

    /**
     * Converts Country entity to CountryDto object.
     *
     * @param country country entity
     * @return converted DTO object
     */
    public CountryDto convertToDto(Country country) {
        CountryDto dto = new CountryDto();

        dto.setId(country.getId());
        dto.setName(country.getName());
        dto.setCreatedOn(country.getCreatedOn());
        dto.setUpdatedOn(country.getUpdatedOn());

        return dto;
    }

    /**
     * Converts CountryDto object to Country entity.
     *
     * @param dto country DTO object
     * @return converted country entity
     */
    private Country convertToEntity(CountryDto dto) {
        Country country = new Country();

        country.setName(dto.getName().trim());

        return country;
    }
}