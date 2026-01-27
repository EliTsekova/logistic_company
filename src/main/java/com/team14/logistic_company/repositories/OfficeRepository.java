package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {

    // Намиране по име/title
    Optional<Office> findByTitle(String title);

    boolean existsByTitle(String title);

    List<Office> findByTitleContainingIgnoreCase(String title);

    // Намиране на офиси по адрес
    Optional<Office> findByAddressId(Integer addressId);

    // Намиране на офиси по град (чрез join с Address и City)
    @Query("SELECT o FROM Office o WHERE o.address.city.id = :cityId")
    List<Office> findByCityId(@Param("cityId") Integer cityId);

    // Намиране на офиси по държава (чрез join с Address, City и Country)
    @Query("SELECT o FROM Office o WHERE o.address.city.country.id = :countryId")
    List<Office> findByCountryId(@Param("countryId") Integer countryId);
}