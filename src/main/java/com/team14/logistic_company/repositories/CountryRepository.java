package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface used for country database operations.
 * Provides methods for searching and validating country entities.
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {

    /**
     * Finds a country by name.
     *
     * @param name the country name
     * @return optional containing the country if found
     */
    Optional<Country> findByName(String name);

    /**
     * Checks whether a country with the given name exists.
     *
     * @param name the country name
     * @return true if country exists
     */
    boolean existsByName(String name);
}