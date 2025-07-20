package com.school21.Tic_Tac_Toe.datasource.repository.game;

import com.school21.Tic_Tac_Toe.datasource.mapper.GameDataMapper;
import com.school21.Tic_Tac_Toe.datasource.model.GameEntity;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GameRepositoryImpl implements GameRepository {
    private final GameJpaRepository gameRepository;

    @Override
    public void saveGame(Game game) {
        gameRepository.save(GameDataMapper.toEntity(game));
    }

    @Override
    public Optional<Game> findById(UUID id) {
        GameEntity gameEntity = gameRepository.findById(id).orElse(null);
        if (gameEntity == null) {
            return Optional.empty();
        } else {
            return Optional.of(GameDataMapper.toModel(gameEntity));
        }
    }

    @Override
    public List<Game> getAvailableGamesForUser(UUID userId) {
        return gameRepository.getGameEntityByStatusAndPlayerXNot(GameStatus.WAITING, userId)
                .stream().map(GameDataMapper::toModel).collect(Collectors.toList());
    }
}
