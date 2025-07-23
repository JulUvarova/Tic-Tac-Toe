package com.school21.Tic_Tac_Toe.di;

import com.school21.Tic_Tac_Toe.web.security.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // убираем сессионку
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(
                                "/auth/register", "/auth/login",
                                "/", "/ui/**", "/css/**", "/js/**", "/images/**", // для фронта
                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**" // для сваггера
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}