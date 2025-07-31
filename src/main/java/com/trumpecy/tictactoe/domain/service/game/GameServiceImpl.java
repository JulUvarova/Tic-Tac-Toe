package com.trumpecy.tictactoe.domain.service.game;

import com.trumpecy.tictactoe.datasource.repository.game.GameRepository;
import com.trumpecy.tictactoe.di.MinimaxAgent;
import com.trumpecy.tictactoe.domain.model.game.Game;
import com.trumpecy.tictactoe.domain.model.game.GameConstant;
import com.trumpecy.tictactoe.domain.model.game.GameStatus;
import com.trumpecy.tictactoe.domain.model.stats.UserRatio;
import com.trumpecy.tictactoe.domain.model.stats.UserStats;
import com.trumpecy.tictactoe.exception.EntityNotFoundException;
import com.trumpecy.tictactoe.exception.InvalidGameIdException;
import com.trumpecy.tictactoe.exception.InvalidMoveException;
import com.trumpecy.tictactoe.web.model.game.OpponentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final MinimaxAgent agent;

    @Override
    public Game getNextMove(UUID gameId, UUID userId, int[][] userBoard) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
        if (!userId.equals(game.getCurrentPlayer())) {
            throw new InvalidGameIdException(String.format("It's not time for user %s move", userId));
        }
        if (game.getPlayerX() == null || game.getPlayerO() == null) {
            throw new InvalidMoveException(
                    String.format("Game %s is not started yet", gameId));
        }
        if (isGameOver(game)) {
            throw new InvalidMoveException(
                    String.format("Game %s ended", gameId));
        }
        if (!GameLogicUtility.isBoardValid(game.getBoard().getMatrix(), userBoard,
                (game.getCurrentPlayer().equals(game.getPlayerX()) ? GameConstant.PLAYER_X : GameConstant.PLAYER_O))) {
            throw new InvalidMoveException(
                    String.format("Invalid user move in game %s", gameId));
        }

        game.getBoard().setMatrix(userBoard);
        game.setStatus(GameLogicUtility.checkGameStatus(game.getBoard().getMatrix()));
        log.info("User  makes a move in game {}, new status: {}", game.getId(), game.getStatus());
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            if (game.getPlayerO().equals(agent.getId())) {
                int[] agentMove = GameLogicUtility.getMove(game.getBoard());
                game.getBoard().getMatrix()[agentMove[0]][agentMove[1]] = GameConstant.PLAYER_O;
                game.setStatus(GameLogicUtility.checkGameStatus(game.getBoard().getMatrix()));
                log.info("Agent makes a move in game {}, new status: {}", game.getId(), game.getStatus());
            } else {
                game.setCurrentPlayer(game.getCurrentPlayer().equals(game.getPlayerX()) ? game.getPlayerO() : game.getPlayerX());
                log.info("New current player {}", game.getCurrentPlayer());
            }
        }
        gameRepository.saveGame(game);
        return game;
    }

    private boolean isGameOver(Game game) {
        return game.getStatus() == GameStatus.O_WINS || game.getStatus() == GameStatus.X_WINS;
    }

    @Override
    public Game createNewGame(UUID userId, OpponentType opponent) {
        Game game = new Game();
        game.setPlayerX(userId);
        game.setCurrentPlayer(userId);
        if (opponent == OpponentType.COMPUTER) {
            game.setPlayerO(agent.getId());
            game.setStatus(GameStatus.IN_PROGRESS);
        } else {
            game.setStatus(GameStatus.WAITING);
        }
        gameRepository.saveGame(game);
        return game;
    }

    @Override
    public Game getGameById(UUID gameId) {
        return gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
    }

    @Override
    public Game joinGame(UUID gameId, UUID userId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
        if (game.getStatus() != GameStatus.WAITING || game.getPlayerO() != null || game.getPlayerX().equals(userId)) {
            throw new InvalidGameIdException(String.format("User %s can't join into game %s", userId, gameId));
        }
        game.setPlayerO(userId);
        game.setCurrentPlayer(game.getPlayerX());
        game.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.saveGame(game);
        return game;
    }

    @Override
    public Page<Game> getAvailableGamesForUserId(UUID userId, int page, int size) {
        return gameRepository.getAvailableGamesForUser(userId, page, size);
    }

    @Override
    public Page<Game> getCurrentGamesByUserId(UUID userId, int page, int size) {
        return gameRepository.getCurrentGamesByUserId(userId, page, size);
    }

    @Override
    public Page<Game> getCompletedGamesByUserId(UUID userId, int page, int size) {
        return gameRepository.getCompletedGamesByUserId(userId, page, size);
    }

    @Override
    public UserStats getUserStats(UUID userId) {
        return gameRepository.getUserStats(userId);
    }

    @Override
    public List<UserRatio> getLeaderBoard(int limit) {
        return gameRepository.getLeaderBoard(limit);
    }
}