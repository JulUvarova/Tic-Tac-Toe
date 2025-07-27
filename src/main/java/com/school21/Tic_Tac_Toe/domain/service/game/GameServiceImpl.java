package com.school21.Tic_Tac_Toe.domain.service.game;

import com.school21.Tic_Tac_Toe.datasource.repository.game.GameRepository;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.model.game.GameConstant;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserRatio;
import com.school21.Tic_Tac_Toe.domain.model.stats.UserStats;
import com.school21.Tic_Tac_Toe.domain.service.game.strategy.MinimaxAgent;
import com.school21.Tic_Tac_Toe.exception.EntityNotFoundException;
import com.school21.Tic_Tac_Toe.exception.InvalidGameIdException;
import com.school21.Tic_Tac_Toe.exception.InvalidMoveException;
import com.school21.Tic_Tac_Toe.web.model.game.OpponentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public Game getNextMove(UUID gameId, UUID userId, int[][] userBoard) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
        if (!userId.equals(game.getCurrentPlayer())) {
            throw new InvalidGameIdException(String.format("It's not time for user %s move", userId));
        }
        if (isGameOver(game)) {
            throw new InvalidMoveException(
                    String.format("Game %s ended", gameId));
        }
        if (!isBoardValid(game.getBoard().getMatrix(), userBoard,
                (game.getCurrentPlayer().equals(game.getPlayerX()) ? GameConstant.PLAYER_X : GameConstant.PLAYER_O))) {
            throw new InvalidMoveException(
                    String.format("Invalid user move in game %s", gameId));
        }

        game.getBoard().setMatrix(userBoard);
        game.updateStatus();
        log.info("User  makes a move in game {}, new status: {}", game.getId(), game.getStatus());
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            if (game.getPlayerO().equals(GameConstant.MINIMAX_AGENT_UUID)) {
                int[] agentMove = MinimaxAgent.getMove(game.getBoard());
                game.getBoard().getMatrix()[agentMove[0]][agentMove[1]] = GameConstant.PLAYER_O;
                game.updateStatus();
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
            game.setPlayerO(GameConstant.MINIMAX_AGENT_UUID);
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
    public List<Game> getAvailableGamesForUserId(UUID userId) {
        return gameRepository.getAvailableGamesForUser(userId);
    }

    @Override
    public List<Game> getCurrentGamesByUserId(UUID userId) {
        return gameRepository.getCurrentGamesByUserId(userId);
    }

    @Override
    public List<Game> getCompletedGamesByUserId(UUID userId) {
        return gameRepository.getCompletedGamesByUserId(userId);
    }

    @Override
    public UserStats getUserStats(UUID userId) {
        return gameRepository.getUserStats(userId);
    }

    @Override
    public List<UserRatio> getLeaderBoard(int limit) {
        return gameRepository.getLeaderBoard(limit);
    }

    private boolean isBoardValid(int[][] prev, int[][] next, int player) {
        if (prev == null || next == null) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                if (prev[i][j] != next[i][j]) {
                    if (prev[i][j] != 0
                            || next[i][j] != player
                            || count > 0) {
                        return false;
                    }
                    count++;
                }
            }
        }
        return count == 1;
    }
}