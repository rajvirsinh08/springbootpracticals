package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //BCrypt Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Authentication Provider
    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    //Security Rules
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

        http
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())

            // URL security rules
            .authorizeHttpRequests(auth -> auth

                // Registration is public
                .requestMatchers("/api/auth/register")
                .permitAll()

                // Profile requires login
                .requestMatchers("/api/auth/profile")
                .authenticated()

                // Delete account requires login
                .requestMatchers("/api/auth/deregister")
                .authenticated()

                // Secure data requires login
                .requestMatchers("/api/secure/**")
                .authenticated()

                // All other requests require login
                .anyRequest()
                .authenticated()
            )

            // Enable HTTP Basic Authentication
            .httpBasic(httpBasic -> {});

        return http.build();
    }
}