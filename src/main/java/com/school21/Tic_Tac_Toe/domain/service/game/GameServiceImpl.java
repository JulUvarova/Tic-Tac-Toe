package com.school21.Tic_Tac_Toe.domain.service.game;

import com.school21.Tic_Tac_Toe.datasource.repository.game.GameRepository;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.model.game.GameConstant;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import com.school21.Tic_Tac_Toe.domain.service.game.strategy.MinimaxAgent;
import com.school21.Tic_Tac_Toe.exception.EntityNotFoundException;
import com.school21.Tic_Tac_Toe.exception.InvalidGameIdException;
import com.school21.Tic_Tac_Toe.web.model.OpponentType;
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
    public Game getNextMove(UUID gameId, int[][] userBoard) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
        game.getBoard().setMatrix(userBoard);
        game.updateStatus();
        log.info("User  makes a move in game {}, new status: {}", game.getId(), game.getStatus());
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            if (game.getPlayerO().equals(GameConstant.AGENT_UUID)) {
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

    @Override
    public boolean validateUserBoard(UUID userId, UUID gameId, int[][] userBoard) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
        if (!userId.equals(game.getCurrentPlayer()) || game.getStatus() != GameStatus.IN_PROGRESS) {
            return false;
        }
        return isBoardValid(game.getBoard().getMatrix(), userBoard, (game.getCurrentPlayer().equals(game.getPlayerX()) ? GameConstant.PLAYER_X : GameConstant.PLAYER_O));
    }

    @Override
    public boolean isGameOver(UUID gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));

        return game.getStatus() == GameStatus.O_WINS || game.getStatus() == GameStatus.X_WINS;
    }

    @Override
    public Game createNewGame(UUID userId, OpponentType opponent) {
        Game game = new Game();
        game.setPlayerX(userId);
        game.setCurrentPlayer(userId);
        if (opponent == OpponentType.COMPUTER) {
            game.setPlayerO(GameConstant.AGENT_UUID);
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
    public List<Game> getAvailableGames(UUID userId) {
        return gameRepository.getAvailableGamesForUser(userId);
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