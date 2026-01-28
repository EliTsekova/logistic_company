package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;

import java.util.List;

public interface IUserService {

    UserDto getByUsername(String username);

    UserDto getById(Integer id);

    User create(UserDto userDto);

    List<UserDto> getUsersByRole(Role role);

    UserDto update(UserDto updatedUser);

    void delete(Integer id);
}
