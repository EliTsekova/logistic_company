package com.team14.logistic_company.service_tests;
import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.entities.enums.Role;
import com.team14.logistic_company.repositories.UserRepository;
import com.team14.logistic_company.services.CustomUserDetails;
import com.team14.logistic_company.services.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CustomUserDetailsService} class.
 *
 * These tests verify the authentication-related
 * functionality of the CustomUserDetailsService.
 */
@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    /**
     * Initializes common test objects before each test.
     */
    @BeforeEach
    void setUp() {

        user = new User();

        user.setUsername("ivan123");
        user.setPassword("password123");
        user.setEmail("ivan@test.com");
        user.setRole(Role.CLIENT);
    }

    /**
     * Tests that a valid user
     * is loaded successfully by username.
     */
    @Test
    void shouldLoadUserByUsername() {

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                customUserDetailsService
                        .loadUserByUsername("ivan123");

        assertNotNull(result);

        assertEquals(
                "ivan123",
                result.getUsername()
        );

        assertEquals(
                "password123",
                result.getPassword()
        );

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("CLIENT")
                        )
        );

        verify(userRepository)
                .findByUsername("ivan123");
    }

    /**
     * Tests that UsernameNotFoundException
     * is thrown when the user does not exist.
     */
    @Test
    void shouldThrowWhenUserNotFound() {

        when(userRepository.findByUsername("missingUser"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService
                        .loadUserByUsername("missingUser")
        );

        verify(userRepository)
                .findByUsername("missingUser");
    }

    /**
     * Tests that the returned UserDetails object
     * contains the correct email information.
     */
    @Test
    void shouldContainCorrectEmail() {

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.of(user));

        CustomUserDetails result =
                (CustomUserDetails)
                        customUserDetailsService
                                .loadUserByUsername("ivan123");

        assertEquals(
                "ivan@test.com",
                result.getEmail()
        );
    }

    /**
     * Tests that the correct authority
     * is assigned to the loaded user.
     */
    @Test
    void shouldAssignCorrectAuthority() {

        when(userRepository.findByUsername("ivan123"))
                .thenReturn(Optional.of(user));

        UserDetails result =
                customUserDetailsService
                        .loadUserByUsername("ivan123");

        assertEquals(
                1,
                result.getAuthorities().size()
        );

        assertTrue(
                result.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("CLIENT")
                        )
        );
    }
}
