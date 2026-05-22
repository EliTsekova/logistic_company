package com.team14.logistic_company.service_tests;

import com.team14.logistic_company.services.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link CustomUserDetails} class.
 *
 * These tests verify the functionality of the
 * CustomUserDetails implementation used by Spring Security.
 */
class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;

    /**
     * Initializes common test data before each test.
     */
    @BeforeEach
    void setUp() {

        List<GrantedAuthority> authorities =
                List.of(
                        new SimpleGrantedAuthority("ROLE_CLIENT")
                );

        customUserDetails =
                new CustomUserDetails(
                        "ivan123",
                        "password123",
                        "ivan@test.com",
                        authorities
                );
    }

    /**
     * Tests that the username
     * is returned correctly.
     */
    @Test
    void shouldReturnUsername() {

        assertEquals(
                "ivan123",
                customUserDetails.getUsername()
        );
    }

    /**
     * Tests that the password
     * is returned correctly.
     */
    @Test
    void shouldReturnPassword() {

        assertEquals(
                "password123",
                customUserDetails.getPassword()
        );
    }

    /**
     * Tests that the email
     * is returned correctly.
     */
    @Test
    void shouldReturnEmail() {

        assertEquals(
                "ivan@test.com",
                customUserDetails.getEmail()
        );
    }

    /**
     * Tests that authorities
     * are returned correctly.
     */
    @Test
    void shouldReturnAuthorities() {

        assertEquals(
                1,
                customUserDetails.getAuthorities().size()
        );

        assertTrue(
                customUserDetails.getAuthorities()
                        .stream()
                        .anyMatch(a ->
                                a.getAuthority()
                                        .equals("ROLE_CLIENT")
                        )
        );
    }

    /**
     * Tests that the account
     * is not expired.
     */
    @Test
    void shouldReturnAccountNonExpired() {

        assertTrue(
                customUserDetails.isAccountNonExpired()
        );
    }

    /**
     * Tests that the account
     * is not locked.
     */
    @Test
    void shouldReturnAccountNonLocked() {

        assertTrue(
                customUserDetails.isAccountNonLocked()
        );
    }

    /**
     * Tests that the credentials
     * are not expired.
     */
    @Test
    void shouldReturnCredentialsNonExpired() {

        assertTrue(
                customUserDetails.isCredentialsNonExpired()
        );
    }

    /**
     * Tests that the account
     * is enabled.
     */
    @Test
    void shouldReturnEnabled() {

        assertTrue(
                customUserDetails.isEnabled()
        );
    }
}