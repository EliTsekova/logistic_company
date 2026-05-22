package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface used for city database operations.
 * Provides methods for searching and validating city entities.
 */
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    /**
     * Returns all cities from a specific country.
     *
     * @param countryId the country identifier
     * @return list of cities
     */
    List<City> findByCountryId(Integer countryId);

    /**
     * Finds a city by name.
     *
     * @param name the city name
     * @return optional containing the city if found
     */
    Optional<City> findByName(String name);

    /**
     * Checks whether a city with the given name exists.
     *
     * @param name the city name
     * @return true if city exists
     */
    boolean existsByName(String name);

    /**
     * Returns all cities containing the given name.
     * The search is case-insensitive.
     *
     * @param name city name or part of it
     * @return list of matching cities
     */
    List<City> findByNameContainingIgnoreCase(String name);
}