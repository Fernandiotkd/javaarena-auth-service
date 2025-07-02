package com.javaarena.java_arena_auth_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Modern way to disable CSRF
                .authorizeHttpRequests(authorize -> authorize // Use authorizeHttpRequests
                        .requestMatchers("/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/newuser").permitAll()
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/newuser/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/master/*").permitAll()
                        .requestMatchers(HttpMethod.GET, "/exploreCourse").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}