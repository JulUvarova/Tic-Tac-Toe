package com.school21.Tic_Tac_Toe.domain.service.game;

import com.school21.Tic_Tac_Toe.datasource.repository.game.GameRepository;
import com.school21.Tic_Tac_Toe.domain.model.game.Board;
import com.school21.Tic_Tac_Toe.domain.model.game.GameConstant;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import com.school21.Tic_Tac_Toe.domain.service.game.strategy.MinimaxAgent;
import com.school21.Tic_Tac_Toe.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public Game getNextMove(UUID gameId, Board userBoard) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
        game.setBoard(userBoard);
        GameStatus status = userBoard.checkGameStatus();
        log.info("User makes a move in game {}, new status: {}", game.getId(), status);
        if (status == GameStatus.IN_PROGRESS) {
            int[] agentMove = MinimaxAgent.getMove(game.getBoard());
            game.getBoard().getMatrix()[agentMove[0]][agentMove[1]] = GameConstant.PLAYER_O;
            log.info("Agent makes a move in game {}, new status: {}", game.getId(), game.getBoard().checkGameStatus());
        }
        gameRepository.saveGame(game);
        return game;
    }

    @Override
    public boolean validateUserBoard(UUID gameId, Board userBoard) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));

        return isBoardValid(game.getBoard(), userBoard);
    }

    @Override
    public boolean isGameOver(UUID gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));

        return game.getBoard().checkGameStatus() != GameStatus.IN_PROGRESS;
    }

    @Override
    public Game createNewGame() {
        Game game = new Game();
        gameRepository.saveGame(game);
        return game;
    }

    @Override
    public Game getGameById(UUID gameId) {
        return gameRepository.findById(gameId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Invalid game id %s", gameId)));
    }

    private boolean isBoardValid(Board prev, Board next) {
        if (prev == null || next == null) {
            return false;
        }

        int count = 0;
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                if (prev.getMatrix()[i][j] != next.getMatrix()[i][j]) {
                    if (prev.getMatrix()[i][j] != 0
                            || next.getMatrix()[i][j] != GameConstant.PLAYER_X
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