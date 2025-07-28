package com.trumpecy.tictactoe.di;

import com.trumpecy.tictactoe.datasource.repository.game.GameJpaRepository;
import com.trumpecy.tictactoe.datasource.repository.game.GameRepository;
import com.trumpecy.tictactoe.datasource.repository.game.GameRepositoryImpl;
import com.trumpecy.tictactoe.datasource.repository.user.UserRepository;
import com.trumpecy.tictactoe.domain.model.game.GameConstant;
import com.trumpecy.tictactoe.domain.model.user.User;
import com.trumpecy.tictactoe.domain.service.game.GameService;
import com.trumpecy.tictactoe.domain.service.game.GameServiceImpl;
import com.trumpecy.tictactoe.domain.service.user.UserService;
import com.trumpecy.tictactoe.domain.service.user.UserServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public GameRepository gameRepository(GameJpaRepository gameJpaRepository) {
        return new GameRepositoryImpl(gameJpaRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner dataLoader(UserRepository userRepository) {
        return args -> userRepository.save(new User(GameConstant.MINIMAX_AGENT_UUID, "Agent_OOO", "1234", null));
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public GameService gameService(GameRepository gameRepository) {
        return new GameServiceImpl(gameRepository);
    }
}
