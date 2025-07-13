package com.school21.Tic_Tac_Toe.datasource.repository;

import com.school21.Tic_Tac_Toe.datasource.mapper.GameDataMapper;
import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InMemGameRepositoryImpl implements GameRepository {
    private final GameMemStorage gameMemStorage;

    @Override
    public void saveGame(GameModel gameModel) {
        gameMemStorage.saveGame(gameModel.getId(), GameDataMapper.toEntity(gameModel));
    }

    @Override
    public Optional<GameModel> findById(UUID id) {
        GameEntity game = gameMemStorage.getGame(id);
        if (game == null) {
            return Optional.empty();
        } else {
            return Optional.of(GameDataMapper.toModel(game));
        }
    }
}
