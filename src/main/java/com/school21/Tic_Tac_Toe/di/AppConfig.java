package com.school21.Tic_Tac_Toe.di;

import com.school21.Tic_Tac_Toe.datasource.repository.GameJpaRepository;
import com.school21.Tic_Tac_Toe.datasource.repository.GameRepository;
import com.school21.Tic_Tac_Toe.datasource.repository.GameRepositoryImpl;
import com.school21.Tic_Tac_Toe.domain.service.GameService;
import com.school21.Tic_Tac_Toe.domain.service.GameServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public GameRepository gameRepository(GameJpaRepository gameJpaRepository) {
        return new GameRepositoryImpl(gameJpaRepository);
    }

    @Bean
    public GameService gameService(GameRepository gameRepository) {
        return new GameServiceImpl(gameRepository);
    }
}
