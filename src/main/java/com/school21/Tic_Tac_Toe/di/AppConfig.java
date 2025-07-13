package com.school21.Tic_Tac_Toe.di;

import com.school21.Tic_Tac_Toe.datasource.repository.GameMemStorage;
import com.school21.Tic_Tac_Toe.datasource.repository.GameRepository;
import com.school21.Tic_Tac_Toe.datasource.repository.InMemGameRepositoryImpl;
import com.school21.Tic_Tac_Toe.domain.service.GameService;
import com.school21.Tic_Tac_Toe.domain.service.GameServiceImpl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    @Bean
    public GameMemStorage gameStorage() {
        return new GameMemStorage();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public GameRepository gameRepository(GameMemStorage gameStorage) {
        return new InMemGameRepositoryImpl(gameStorage);
    }

    @Bean
    public GameService gameService(GameRepository gameRepository) {
        return new GameServiceImpl(gameRepository);
    }
}
