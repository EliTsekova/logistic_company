package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface used for office database operations.
 * Provides methods for searching and validating office entities.
 */
@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {

    /**
     * Finds an office by title.
     *
     * @param title the office title
     * @return optional containing the office if found
     */
    Optional<Office> findByTitle(String title);

    /**
     * Checks whether an office with the given title exists.
     *
     * @param title the office title
     * @return true if office exists
     */
    boolean existsByTitle(String title);

    /**
     * Returns all offices containing the given title.
     * The search is case-insensitive.
     *
     * @param title office title or part of it
     * @return list of matching offices
     */
    List<Office> findByTitleContainingIgnoreCase(String title);

    /**
     * Finds an office by address identifier.
     *
     * @param addressId the address identifier
     * @return optional containing the office if found
     */
    Optional<Office> findByAddressId(Integer addressId);

    /**
     * Returns all offices located in a specific city.
     *
     * @param cityId the city identifier
     * @return list of offices
     */
    @Query("SELECT o FROM Office o WHERE o.address.city.id = :cityId")
    List<Office> findByCityId(@Param("cityId") Integer cityId);

    /**
     * Returns all offices located in a specific country.
     *
     * @param countryId the country identifier
     * @return list of offices
     */
    @Query("SELECT o FROM Office o WHERE o.address.city.country.id = :countryId")
    List<Office> findByCountryId(@Param("countryId") Integer countryId);
}