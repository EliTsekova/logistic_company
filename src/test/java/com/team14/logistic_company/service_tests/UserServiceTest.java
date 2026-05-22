package com.team14.logistic_company.service_tests;
import com.team14.logistic_company.dtos.UserDto;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.repositories.UserRepository;
import com.team14.logistic_company.services.UserService;
import com.team14.logistic_company.services.exceptions.EmailNotAvailable;
import com.team14.logistic_company.services.exceptions.UserNotFound;
import com.team14.logistic_company.services.exceptions.UsernameNotAvailable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link UserService} class.
 *
 * These tests verify user creation, update, deletion,
 * DTO conversion and repository interactions.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        user = new User();
        user.setUsername("ivan123");
        user.setEmail("ivan@test.com");
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setRole(Role.CLIENT);
        user.setPassword("encodedPassword");

        userDto = new UserDto();
        userDto.setUsername("ivan123");
        userDto.setEmail("ivan@test.com");
        userDto.setFirstName("Ivan");
        userDto.setLastName("Ivanov");
        userDto.setRole(Role.CLIENT);
        userDto.setPassword("plainPassword");
    }

    /**
     * Tests that a user is found successfully by username.
     */
    @Test
    void shouldGetUserByUsername() {

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.of(user));

        UserDto result =
                userService.getByUsername("ivan123");

        assertNotNull(result);
        assertEquals("ivan123", result.getUsername());
        assertEquals("ivan@test.com", result.getEmail());

        verify(userRepository)
                .findByUsername("ivan123");
    }

    /**
     * Tests that null is returned
     * when username does not exist.
     */
    @Test
    void shouldReturnNullWhenUsernameNotFound() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        UserDto result =
                userService.getByUsername("missing");

        assertNull(result);
    }

    /**
     * Tests that a user is found successfully by ID.
     */
    @Test
    void shouldGetUserById() {

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        UserDto result =
                userService.getById(1);

        assertNotNull(result);
        assertEquals("Ivan", result.getFirstName());
        assertEquals(Role.CLIENT, result.getRole());

        verify(userRepository)
                .findById(1);
    }

    /**
     * Tests that UserNotFound is thrown
     * when user ID does not exist.
     */
    @Test
    void shouldThrowWhenUserByIdNotFound() {

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFound.class,
                () -> userService.getById(1)
        );
    }

    /**
     * Tests that a new user is created successfully.
     */
    @Test
    void shouldCreateUser() {

        when(userRepository.existsByUsername("ivan123"))
                .thenReturn(false);

        when(userRepository.existsByEmail("ivan@test.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        User result =
                userService.create(userDto);

        assertNotNull(result);
        assertEquals("ivan123", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository)
                .save(any(User.class));
    }

    /**
     * Tests that the default CLIENT role is assigned
     * when no role is provided during creation.
     */
    @Test
    void shouldCreateUserWithDefaultClientRole() {

        userDto.setRole(null);

        when(userRepository.existsByUsername("ivan123"))
                .thenReturn(false);

        when(userRepository.existsByEmail("ivan@test.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result =
                userService.create(userDto);

        assertEquals(Role.CLIENT, result.getRole());
    }

    /**
     * Tests that UsernameNotAvailable is thrown
     * when username already exists.
     */
    @Test
    void shouldThrowWhenUsernameIsNotAvailable() {

        when(userRepository.existsByUsername("ivan123"))
                .thenReturn(true);

        assertThrows(
                UsernameNotAvailable.class,
                () -> userService.create(userDto)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    /**
     * Tests that EmailNotAvailable is thrown
     * when email already exists.
     */
    @Test
    void shouldThrowWhenEmailIsNotAvailable() {

        when(userRepository.existsByUsername("ivan123"))
                .thenReturn(false);

        when(userRepository.existsByEmail("ivan@test.com"))
                .thenReturn(true);

        assertThrows(
                EmailNotAvailable.class,
                () -> userService.create(userDto)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    /**
     * Tests that users are found successfully by role.
     */
    @Test
    void shouldGetUsersByRole() {

        when(userRepository.findByRole(Role.CLIENT))
                .thenReturn(List.of(user));

        List<UserDto> result =
                userService.getUsersByRole(Role.CLIENT);

        assertEquals(1, result.size());
        assertEquals(Role.CLIENT, result.get(0).getRole());

        verify(userRepository)
                .findByRole(Role.CLIENT);
    }

    /**
     * Tests that an existing user is updated successfully.
     */
    @Test
    void shouldUpdateUser() {

        userDto.setId(1);
        userDto.setUsername("newuser");
        userDto.setEmail("new@test.com");
        userDto.setPassword("newPassword");

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(userRepository.findByUsername("newuser"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("new@test.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("newPassword"))
                .thenReturn("newEncodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserDto result =
                userService.update(userDto);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("new@test.com", result.getEmail());

        verify(userRepository)
                .save(user);
    }

    /**
     * Tests that IllegalArgumentException is thrown
     * when update DTO is null.
     */
    @Test
    void shouldThrowWhenUpdatingNullUser() {

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.update(null)
        );
    }

    /**
     * Tests that IllegalArgumentException is thrown
     * when update DTO has no ID.
     */
    @Test
    void shouldThrowWhenUpdatingUserWithoutId() {

        userDto.setId(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.update(userDto)
        );
    }

    /**
     * Tests that UserNotFound is thrown
     * when updating non-existing user.
     */
    @Test
    void shouldThrowWhenUpdatingInvalidUser() {

        userDto.setId(1);

        when(userRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFound.class,
                () -> userService.update(userDto)
        );
    }

    /**
     * Tests that UsernameNotAvailable is thrown
     * when updating with another user's username.
     */
    @Test
    void shouldThrowWhenUpdatingWithTakenUsername() {

        User anotherUser = mock(User.class);

        when(anotherUser.getId())
                .thenReturn(2);

        userDto.setId(1);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.of(anotherUser));

        assertThrows(
                UsernameNotAvailable.class,
                () -> userService.update(userDto)
        );
    }

    /**
     * Tests that EmailNotAvailable is thrown
     * when updating with another user's email.
     */
    @Test
    void shouldThrowWhenUpdatingWithTakenEmail() {

        User anotherUser = mock(User.class);

        when(anotherUser.getId())
                .thenReturn(2);

        userDto.setId(1);

        when(userRepository.findById(1))
                .thenReturn(Optional.of(user));

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("ivan@test.com"))
                .thenReturn(Optional.of(anotherUser));

        assertThrows(
                EmailNotAvailable.class,
                () -> userService.update(userDto)
        );
    }

    /**
     * Tests that a user is deleted successfully.
     */
    @Test
    void shouldDeleteUser() {

        when(userRepository.existsById(1))
                .thenReturn(true);

        userService.delete(1);

        verify(userRepository)
                .deleteById(1);
    }

    /**
     * Tests that UserNotFound is thrown
     * when deleting non-existing user.
     */
    @Test
    void shouldThrowWhenDeletingInvalidUser() {

        when(userRepository.existsById(1))
                .thenReturn(false);

        assertThrows(
                UserNotFound.class,
                () -> userService.delete(1)
        );

        verify(userRepository, never())
                .deleteById(1);
    }
}