package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface used for address database operations.
 * Provides methods for searching addresses by different criteria.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    /**
     * Returns all addresses from a specific city.
     *
     * @param cityId the city identifier
     * @return list of addresses
     */
    List<Address> findByCityId(Integer cityId);

    /**
     * Returns all addresses containing the given street name.
     * The search is case-insensitive.
     *
     * @param street street name or part of it
     * @return list of matching addresses
     */
    List<Address> findByStreetContainingIgnoreCase(String street);

    /**
     * Returns all addresses with the specified postal code.
     *
     * @param postalCode postal code
     * @return list of addresses
     */
    List<Address> findByPostalCode(String postalCode);

    /**
     * Returns all addresses from a specific city
     * with the given postal code.
     *
     * @param cityId the city identifier
     * @param postalCode postal code
     * @return list of matching addresses
     */
    List<Address> findByCityIdAndPostalCode(Integer cityId, String postalCode);
}