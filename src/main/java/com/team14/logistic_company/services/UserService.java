package com.team14.logistic_company.services;

import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.repositories.UserRepository;
import com.team14.logistic_company.services.exceptions.EmailNotAvailable;
import com.team14.logistic_company.services.exceptions.UserNotFound;
import com.team14.logistic_company.services.exceptions.UsernameNotAvailable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toDto)
                .orElse(null);
    }

    @Override
    public UserDto getById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound("User not found with id: " + id));
        return toDto(user);
    }

    @Override
    public User create(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameNotAvailable("Username is not available: " + userDto.getUsername());
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailNotAvailable("Email is not available: " + userDto.getEmail());
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(userDto.getRole() == null ? Role.CLIENT : userDto.getRole());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public List<UserDto> getUsersByRole(Role role) {
        return userRepository.findByRole(role).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserDto update(UserDto updatedUser) {
        if (updatedUser == null || updatedUser.getId() == null) {
            throw new IllegalArgumentException("User id is required.");
        }

        User existing = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new UserNotFound("User not found with id: " + updatedUser.getId()));

        userRepository.findByUsername(updatedUser.getUsername()).ifPresent(u -> {
            if (!u.getId().equals(updatedUser.getId())) {
                throw new UsernameNotAvailable("Username is not available: " + updatedUser.getUsername());
            }
        });

        userRepository.findByEmail(updatedUser.getEmail()).ifPresent(u -> {
            if (!u.getId().equals(updatedUser.getId())) {
                throw new EmailNotAvailable("Email is not available: " + updatedUser.getEmail());
            }
        });

        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        existing.setFirstName(updatedUser.getFirstName());
        existing.setLastName(updatedUser.getLastName());

        if (updatedUser.getRole() != null) {
            existing.setRole(updatedUser.getRole());
        }
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return toDto(userRepository.save(existing));
    }

    @Override
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFound("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}
