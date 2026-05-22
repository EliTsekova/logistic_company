package com.team14.logistic_company.services;

import com.team14.logistic_company.entities.User;
import com.team14.logistic_company.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for loading user authentication data.
 *
 * <p>
 * This class implements Spring Security's UserDetailsService interface
 * and is used during the authentication process.
 * </p>
 *
 * <p>
 * The service retrieves user information from the database
 * and converts it into a CustomUserDetails object
 * used by Spring Security.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository used for accessing user data.
     */
    private final UserRepository userRepository;

    /**
     * Loads user details by username.
     *
     * <p>
     * The method searches for a user in the database
     * and creates a CustomUserDetails object containing:
     * </p>
     * <ul>
     *     <li>Username</li>
     *     <li>Password</li>
     *     <li>Email</li>
     *     <li>User roles and authorities</li>
     * </ul>
     *
     * @param username username used for authentication
     * @return authenticated user details object
     * @throws UsernameNotFoundException if user does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new CustomUserDetails(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}