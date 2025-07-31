package com.trumpecy.tictactoe.di;

import com.trumpecy.tictactoe.datasource.repository.user.UserRepository;
import com.trumpecy.tictactoe.domain.model.user.User;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner dataLoader(UserRepository userRepository, MinimaxAgent agent) {
        return args -> userRepository.save(new User(agent.getId(), agent.getName(), "1234", Set.of()));
    }
}