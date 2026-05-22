package com.team14.logistic_company.repositories;

import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface used for user database operations.
 * Provides methods for searching and validating user entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by username.
     *
     * @param username the username
     * @return optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by email address.
     *
     * @param email the email address
     * @return optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Returns all users with a specific role.
     *
     * @param role the user role
     * @return list of users
     */
    List<User> findByRole(Role role);

    /**
     * Checks whether a user with the given username exists.
     *
     * @param username the username
     * @return true if user exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether a user with the given email address exists.
     *
     * @param email the email address
     * @return true if user exists
     */
    boolean existsByEmail(String email);
}
