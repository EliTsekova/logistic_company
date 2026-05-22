package com.team14.logistic_company.configurations;

import com.team14.logistic_company.entities.enums.PositionType;
import com.team14.logistic_company.services.CustomUserDetailsService;
import com.team14.logistic_company.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * Security configuration class for the logistic company application.
 * <p>
 * Defines authentication, password encoding, URL authorization rules,
 * login behavior, logout behavior, and custom handling for API authentication errors.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Service used by Spring Security to load user authentication details.
     */
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * Service used to retrieve employee information after successful login.
     */
    private final EmployeeService employeeService;

    /**
     * Creates the password encoder used for hashing and verifying user passwords.
     *
     * @return BCrypt password encoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates the authentication provider used for username and password authentication.
     * <p>
     * The provider uses {@link CustomUserDetailsService} to load user details
     * and {@link PasswordEncoder} to verify encrypted passwords.
     * </p>
     *
     * @return configured DAO authentication provider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    /**
     * Provides the authentication manager used by Spring Security.
     *
     * @param config authentication configuration provided by Spring
     * @return authentication manager instance
     * @throws Exception if the authentication manager cannot be created
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    /**
     * Configures the main security filter chain of the application.
     * <p>
     * Defines access permissions for public pages, admin pages,
     * employee pages, client pages, shipment pages, login, logout,
     * and API error handling.
     * </p>
     *
     * @param http HTTP security configuration object
     * @return configured security filter chain
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth

                        /*
                         * Allows access to static resources such as CSS,
                         * JavaScript files, images, and webjars.
                         */
                        .requestMatchers(
                                PathRequest.toStaticResources().atCommonLocations()
                        ).permitAll()

                        /*
                         * Allows public access to home, login,
                         * registration, and error pages.
                         */
                        .requestMatchers(
                                "/",
                                "/home",
                                "/login",
                                "/register",
                                "/error"
                        ).permitAll()

                        /*
                         * Allows authenticated clients, employees, and admins
                         * to access the shipment price calculation endpoint.
                         */
                        .requestMatchers("/shipments/calculate-price")
                        .hasAnyAuthority("CLIENT", "EMPLOYEE", "ADMIN")

                        /*
                         * Allows only administrators to access
                         * the admin dashboard.
                         */
                        .requestMatchers("/Admin")
                        .hasAuthority("ADMIN")

                        /*
                         * Allows only administrators to manage company data.
                         */
                        .requestMatchers("/company/**")
                        .hasAuthority("ADMIN")

                        /*
                         * Allows only administrators to manage clients.
                         */
                        .requestMatchers("/clients/**")
                        .hasAuthority("ADMIN")

                        /*
                         * Allows only administrators to manage offices.
                         */
                        .requestMatchers("/offices/**")
                        .hasAuthority("ADMIN")

                        /*
                         * Allows only administrators to create and delete employees.
                         */
                        .requestMatchers(
                                "/employees/new",
                                "/employees/delete/**"
                        )
                        .hasAuthority("ADMIN")

                        /*
                         * Allows administrators and employees to view
                         * and update employee information.
                         */
                        .requestMatchers("/employees/**")
                        .hasAnyAuthority("ADMIN", "EMPLOYEE")

                        /*
                         * Allows employees to access employee dashboard pages.
                         */
                        .requestMatchers(
                                "/Employee",
                                "/Employee/**"
                        )
                        .hasAuthority("EMPLOYEE")

                        /*
                         * Allows employees to access coordinator pages.
                         */
                        .requestMatchers(
                                "/Coordinator",
                                "/Coordinator/**"
                        )
                        .hasAuthority("EMPLOYEE")

                        /*
                         * Allows employees to access deliveryman pages.
                         */
                        .requestMatchers(
                                "/Deliveryman",
                                "/Deliveryman/**"
                        )
                        .hasAuthority("EMPLOYEE")

                        /*
                         * Allows clients to access client pages.
                         */
                        .requestMatchers(
                                "/Client",
                                "/Client/**"
                        )
                        .hasAuthority("CLIENT")

                        /*
                         * Requires authentication for shipment API endpoints.
                         */
                        .requestMatchers("/shipments/api/**")
                        .authenticated()

                        /*
                         * Allows admins, employees, and clients to access
                         * shipment-related pages.
                         */
                        .requestMatchers("/shipments/**")
                        .hasAnyAuthority("ADMIN", "EMPLOYEE", "CLIENT")

                        /*
                         * Requires authentication for all other requests.
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * Returns HTTP 401 Unauthorized for unauthenticated
                 * requests to shipment API endpoints.
                 */
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                request -> request.getRequestURI()
                                        .startsWith("/shipments/api/")
                        )
                )

                /*
                 * Configures custom login page, login parameters,
                 * failure URL, and role-based redirection after login.
                 */
                .formLogin(form -> form

                        .loginPage("/login")
                        .loginProcessingUrl("/login")

                        .usernameParameter("username")
                        .passwordParameter("password")

                        .failureUrl("/login?error")

                        .successHandler((req, res, authentication) -> {

                            boolean isAdmin = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));

                            boolean isEmployee = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a -> a.getAuthority().equals("EMPLOYEE"));

                            boolean isClient = authentication.getAuthorities()
                                    .stream()
                                    .anyMatch(a -> a.getAuthority().equals("CLIENT"));

                            if (isAdmin) {

                                res.sendRedirect("/Admin");

                            } else if (isEmployee) {

                                var employee =
                                        employeeService.findByUsername(authentication.getName());

                                if (employee.getPositionType()
                                        == PositionType.COORDINATOR) {

                                    res.sendRedirect("/Coordinator");

                                } else if (employee.getPositionType()
                                        == PositionType.DELIVERYMAN) {

                                    res.sendRedirect("/Deliveryman");

                                } else {

                                    res.sendRedirect("/Employee");
                                }

                            } else if (isClient) {

                                res.sendRedirect("/Client");

                            } else {

                                res.sendRedirect("/login?error");
                            }
                        })

                        .permitAll()
                )

                /*
                 * Configures logout URL and redirect page after logout.
                 */
                .logout(logout -> logout

                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")

                        .permitAll()
                );

        return http.build();
    }
}