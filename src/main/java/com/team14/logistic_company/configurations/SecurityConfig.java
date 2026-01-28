package com.team14.logistic_company.configurations;

import com.team14.logistic_company.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth

                        // публични
                        .requestMatchers(
                                "/", "/home", "/login", "/register",
                                "/css/**", "/js/**", "/images/**", "/assets/**"
                        ).permitAll()

                        // Logistic company
                        .requestMatchers(HttpMethod.GET, "/company").authenticated()
                        .requestMatchers("/company/edit", "/company/reset").hasAnyAuthority("EMPLOYEE", "ADMIN")

                        // Shipments - общи (и клиент, и служител могат да виждат)
                        .requestMatchers(HttpMethod.GET, "/shipments/**").authenticated()

                        // Shipments - само служител (create/edit/delete/status)
                        .requestMatchers(HttpMethod.GET,  "/shipments/new").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/shipments").hasAnyAuthority("EMPLOYEE", "ADMIN")

                        .requestMatchers(HttpMethod.GET,  "/shipments/*/edit").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/shipments/*").hasAnyAuthority("EMPLOYEE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/shipments/*/delete").hasAnyAuthority("EMPLOYEE", "ADMIN")

                        // реалният ти endpoint за статус е /shipments/{id}/status
                        .requestMatchers(HttpMethod.POST, "/shipments/*/status").hasAnyAuthority("EMPLOYEE", "ADMIN")

                        // ако имаш endpoint само за служители като "/shipments/all" (ако още го ползваш)
                        .requestMatchers(HttpMethod.GET, "/shipments/all").hasAnyAuthority("EMPLOYEE", "ADMIN")

                        // всичко останало - логнат потребител
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

        return http.build();
    }
}
