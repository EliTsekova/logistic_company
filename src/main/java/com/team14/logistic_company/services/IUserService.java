/**
 * Service interface for managing User operations.
 *
 * <p>
 * Defines the contract for user-related functionality such as
 * retrieval, creation, update, deletion, and role-based queries.
 * </p>
 */
package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;

import java.util.List;

public interface IUserService {

    /**
     * Finds a user by username.
     *
     * @param username username
     * @return UserDto object
     */
    UserDto getByUsername(String username);

    /**
     * Finds a user by id.
     *
     * @param id user identifier
     * @return UserDto object
     */
    UserDto getById(Integer id);

    /**
     * Creates a new user.
     *
     * @param userDto user data
     * @return created User entity
     */
    User create(UserDto userDto);

    /**
     * Retrieves users by role.
     *
     * @param role user role
     * @return list of UserDto objects
     */
    List<UserDto> getUsersByRole(Role role);

    /**
     * Updates an existing user.
     *
     * @param updatedUser updated user data
     * @return updated UserDto object
     */
    UserDto update(UserDto updatedUser);

    /**
     * Deletes a user by id.
     *
     * @param id user identifier
     */
    void delete(Integer id);
}