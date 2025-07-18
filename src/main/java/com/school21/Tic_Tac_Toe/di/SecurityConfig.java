package com.school21.Tic_Tac_Toe.di;

import com.school21.Tic_Tac_Toe.web.security.AuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                        .requestMatchers("/auth/register", "/auth/login").permitAll() // Разрешаем регистрацию и логин без авторизации
                        .anyRequest().authenticated() // Все остальные требуют авторизации
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Вставляем ваш фильтр перед стандартным

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}