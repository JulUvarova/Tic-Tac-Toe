package com.school21.Tic_Tac_Toe.di;

import com.school21.Tic_Tac_Toe.web.security.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF для REST API
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/auth/register", "/auth/login",
                                "/", "/ui/**", // для фронта
                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**" // для сваггера
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}