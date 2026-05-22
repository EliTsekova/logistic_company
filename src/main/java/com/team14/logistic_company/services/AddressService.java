package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.AddressDto;
import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.City;
import com.team14.logistic_company.services.exceptions.AddressNotFound;
import com.team14.logistic_company.services.exceptions.CityNotFound;
import com.team14.logistic_company.repositories.AddressRepository;
import com.team14.logistic_company.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation responsible for address management operations.
 *
 * <p>
 * This service provides functionality for:
 * </p>
 * <ul>
 *     <li>Creating addresses</li>
 *     <li>Updating existing addresses</li>
 *     <li>Deleting addresses</li>
 *     <li>Retrieving addresses by id or city</li>
 *     <li>Converting entities to DTOs and vice versa</li>
 * </ul>
 *
 * <p>
 * The service communicates with repositories and validates
 * related city information before persisting data.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AddressService implements IAddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;

    /**
     * Retrieves all addresses from the system.
     *
     * @return list of all addresses as DTO objects
     */
    @Override
    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds an address by its identifier.
     *
     * @param id address identifier
     * @return address DTO object
     * @throws AddressNotFound if address does not exist
     */
    @Override
    public AddressDto findById(Integer id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFound(id));
        return convertToDto(address);
    }

    /**
     * Retrieves all addresses belonging to a specific city.
     *
     * @param cityId city identifier
     * @return list of addresses in the specified city
     */
    @Override
    public List<AddressDto> findByCityId(Integer cityId) {
        return addressRepository.findByCityId(cityId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new address in the system.
     *
     * @param addressDto DTO containing address information
     * @return created address as DTO object
     * @throws CityNotFound if city does not exist
     */
    @Override
    public AddressDto create(AddressDto addressDto) {
        City city = cityRepository.findById(addressDto.getCityId())
                .orElseThrow(() -> new CityNotFound(addressDto.getCityId()));

        Address address = convertToEntity(addressDto);
        address.setCity(city);

        Address saved = addressRepository.save(address);
        return convertToDto(saved);
    }

    /**
     * Updates existing address information.
     *
     * @param id address identifier
     * @param addressDto DTO containing updated data
     * @return updated address as DTO object
     * @throws AddressNotFound if address does not exist
     * @throws CityNotFound if city does not exist
     */
    @Override
    public AddressDto update(Integer id, AddressDto addressDto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFound(id));

        City city = cityRepository.findById(addressDto.getCityId())
                .orElseThrow(() -> new CityNotFound(addressDto.getCityId()));

        address.setStreet(addressDto.getStreet());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCity(city);

        Address updated = addressRepository.save(address);
        return convertToDto(updated);
    }

    /**
     * Deletes an address from the system.
     *
     * @param id address identifier
     * @throws AddressNotFound if address does not exist
     */
    @Override
    public void delete(Integer id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressNotFound(id);
        }
        addressRepository.deleteById(id);
    }

    /**
     * Converts Address entity to AddressDto object.
     *
     * @param address address entity
     * @return converted DTO object
     */
    private AddressDto convertToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setPostalCode(address.getPostalCode());
        dto.setCityId(address.getCity().getId());
        dto.setCreatedOn(address.getCreatedOn());
        dto.setUpdatedOn(address.getUpdatedOn());
        return dto;
    }

    /**
     * Converts AddressDto object to Address entity.
     *
     * @param dto address DTO object
     * @return converted address entity
     */
    private Address convertToEntity(AddressDto dto) {
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
        return address;
    }
}