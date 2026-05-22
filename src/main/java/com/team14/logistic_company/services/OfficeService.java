/**
 * Service implementation responsible for office management operations.
 *
 * <p>
 * This service provides functionality for:
 * </p>
 * <ul>
 *     <li>Retrieving all offices</li>
 *     <li>Finding offices by id</li>
 *     <li>Creating new offices</li>
 *     <li>Updating existing offices</li>
 *     <li>Deleting offices</li>
 *     <li>Mapping Office entities to DTO objects</li>
 * </ul>
 *
 * <p>
 * The service communicates with office and address repositories
 * and ensures that each office is linked to a valid address.
 * </p>
 */
package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.OfficeDto;
import com.team14.logistic_company.entities.Address;
import com.team14.logistic_company.entities.Office;
import com.team14.logistic_company.services.exceptions.AddressNotFound;
import com.team14.logistic_company.services.exceptions.OfficeNotFound;
import com.team14.logistic_company.repositories.AddressRepository;
import com.team14.logistic_company.repositories.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OfficeService implements IOfficeService {

    private final OfficeRepository officeRepository;
    private final AddressRepository addressRepository;

    /**
     * Retrieves all offices from the system.
     *
     * @return list of OfficeDto objects
     */
    @Override
    public List<OfficeDto> findAll() {
        return officeRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Finds an office by its identifier.
     *
     * @param id office identifier
     * @return OfficeDto object
     * @throws OfficeNotFound if office does not exist
     */
    @Override
    public OfficeDto findById(Integer id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFound(id));
        return convertToDto(office);
    }

    /**
     * Creates a new office in the system.
     *
     * @param officeDto DTO containing office data
     * @return created OfficeDto object
     * @throws AddressNotFound if address does not exist
     */
    @Override
    public OfficeDto create(OfficeDto officeDto) {
        Address address = addressRepository.findById(officeDto.getAddressId())
                .orElseThrow(() -> new AddressNotFound(officeDto.getAddressId()));

        Office office = convertToEntity(officeDto);
        office.setAddress(address);

        Office saved = officeRepository.save(office);
        return convertToDto(saved);
    }

    /**
     * Updates an existing office.
     *
     * @param id office identifier
     * @param officeDto updated office data
     * @return updated OfficeDto object
     * @throws OfficeNotFound if office does not exist
     * @throws AddressNotFound if address does not exist
     */
    @Override
    public OfficeDto update(Integer id, OfficeDto officeDto) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new OfficeNotFound(id));

        Address address = addressRepository.findById(officeDto.getAddressId())
                .orElseThrow(() -> new AddressNotFound(officeDto.getAddressId()));

        office.setTitle(officeDto.getTitle());
        office.setAddress(address);

        Office updated = officeRepository.save(office);
        return convertToDto(updated);
    }

    /**
     * Deletes an office from the system.
     *
     * @param id office identifier
     * @throws OfficeNotFound if office does not exist
     */
    @Override
    public void delete(Integer id) {
        if (!officeRepository.existsById(id)) {
            throw new OfficeNotFound(id);
        }

        officeRepository.deleteById(id);
    }

    /**
     * Converts Office entity to OfficeDto object.
     *
     * @param office office entity
     * @return converted DTO object
     */
    private OfficeDto convertToDto(Office office) {
        OfficeDto dto = new OfficeDto();

        dto.setId(office.getId());
        dto.setTitle(office.getTitle());

        if (office.getAddress() != null) {
            dto.setAddressId(office.getAddress().getId());

            if (office.getAddress().getCity() != null) {
                dto.setCityId(office.getAddress().getCity().getId());
            }
        }

        dto.setCreatedOn(office.getCreatedOn());
        dto.setUpdatedOn(office.getUpdatedOn());

        return dto;
    }

    /**
     * Converts OfficeDto object to Office entity.
     *
     * @param dto office DTO object
     * @return converted Office entity
     */
    private Office convertToEntity(OfficeDto dto) {
        Office office = new Office();
        office.setTitle(dto.getTitle());
        return office;
    }
}