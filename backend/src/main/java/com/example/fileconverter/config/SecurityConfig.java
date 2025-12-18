package com.example.fileconverter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 🚨 FIX FOR 403: Disable CSRF protection. This is standard for stateless APIs 
            .csrf(csrf -> csrf.disable())
            
            // Allow all requests to pass through (including /upload and /hello)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll() 
                .anyRequest().authenticated()
            );
            
        return http.build();
    }
}