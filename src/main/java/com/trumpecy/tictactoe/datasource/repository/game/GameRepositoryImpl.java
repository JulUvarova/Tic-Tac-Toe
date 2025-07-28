package com.trumpecy.tictactoe.datasource.repository.game;

import com.trumpecy.tictactoe.datasource.mapper.GameDataMapper;
import com.trumpecy.tictactoe.datasource.mapper.StatsDataMapper;
import com.trumpecy.tictactoe.datasource.model.GameEntity;
import com.trumpecy.tictactoe.domain.model.game.Game;
import com.trumpecy.tictactoe.domain.model.game.GameStatus;
import com.trumpecy.tictactoe.domain.model.stats.UserRatio;
import com.trumpecy.tictactoe.domain.model.stats.UserStats;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
        return gameRepository.getGameEntityByStatusAndPlayerXNot(
                        GameStatus.WAITING,
                        userId,
                        Sort.by(Sort.Direction.ASC, "startTime"))
                .stream()
                .map(GameDataMapper::toModel)
                .toList();
    }

    @Override
    public List<Game> getCurrentGamesByUserId(UUID userId) {
        return gameRepository.findGamesByStatusInAndPlayer(
                        List.of(GameStatus.WAITING, GameStatus.IN_PROGRESS),
                        userId,
                        Sort.by(Sort.Direction.ASC, "startTime"))
                .stream()
                .map(GameDataMapper::toModel)
                .toList();
    }

    @Override
    public List<Game> getCompletedGamesByUserId(UUID userId) {
        return gameRepository.findGamesByStatusInAndPlayer(
                        List.of(GameStatus.DRAW, GameStatus.O_WINS, GameStatus.X_WINS),
                        userId,
                        Sort.by(Sort.Direction.ASC, "startTime"))
                .stream()
                .map(GameDataMapper::toModel)
                .toList();
    }

    @Override
    public UserStats getUserStats(UUID userId) {
        return StatsDataMapper.toStatsModel(gameRepository.getStatsByUserId(userId));
    }

    @Override
    public List<UserRatio> getLeaderBoard(int limit) {
        return gameRepository.getLeaderBoard(limit)
                .stream()
                .map(StatsDataMapper::toRatioModel)
                .toList();
    }
}
