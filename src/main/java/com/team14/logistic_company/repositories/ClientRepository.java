package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface used for client database operations.
 * Provides methods for searching and validating client entities.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    /**
     * Finds a client by associated user identifier.
     *
     * @param userId the user identifier
     * @return optional containing the client if found
     */
    Optional<Client> findByUserId(Integer userId);

    /**
     * Finds a client by phone number.
     *
     * @param phoneNumber the client's phone number
     * @return optional containing the client if found
     */
    Optional<Client> findByPhoneNumber(String phoneNumber);

    /**
     * Checks whether a client with the given user identifier exists.
     *
     * @param userId the user identifier
     * @return true if client exists
     */
    boolean existsByUserId(Integer userId);

    /**
     * Checks whether a client with the given phone number exists.
     *
     * @param phoneNumber the phone number
     * @return true if client exists
     */
    boolean existsByPhoneNumber(String phoneNumber);
}