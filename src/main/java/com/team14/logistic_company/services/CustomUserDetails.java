package com.team14.logistic_company.services;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Custom implementation of Spring Security UserDetails interface.
 *
 * <p>
 * This class represents the authenticated user within the security context.
 * It stores user credentials, email information, and granted authorities.
 * </p>
 *
 * <p>
 * Spring Security uses this class during authentication and authorization
 * processes to validate users and determine their access permissions.
 * </p>
 */
public class CustomUserDetails implements UserDetails {

    /**
     * Username used for authentication.
     */
    private final String username;

    /**
     * Encoded user password.
     */
    private final String password;

    /**
     * User email address.
     */
    @Getter
    private final String email;

    /**
     * Collection of granted authorities and roles.
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * Creates a new CustomUserDetails instance.
     *
     * @param username user username
     * @param password encoded user password
     * @param email user email address
     * @param authorities granted user authorities and roles
     */
    public CustomUserDetails(String username, String password, String email,
                             Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    /**
     * Returns all granted authorities assigned to the user.
     *
     * @return collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the encoded user password.
     *
     * @return user password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username used for authentication.
     *
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user account has expired.
     *
     * @return true if account is not expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user account is locked.
     *
     * @return true if account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user credentials have expired.
     *
     * @return true if credentials are valid
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user account is enabled.
     *
     * @return true if account is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}