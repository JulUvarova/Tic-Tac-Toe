package com.school21.Tic_Tac_Toe.domain.service;

import com.school21.Tic_Tac_Toe.datasource.repository.GameRepository;
import com.school21.Tic_Tac_Toe.domain.model.BoardModel;
import com.school21.Tic_Tac_Toe.domain.model.GameConstant;
import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import com.school21.Tic_Tac_Toe.domain.model.GameStatus;
import com.school21.Tic_Tac_Toe.domain.strategy.MinimaxAgent;
import com.school21.Tic_Tac_Toe.exception.GameNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public GameModel getNextMove(UUID gameId, BoardModel userBoard) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() ->
                new GameNotFoundException(String.format("Invalid game id %s", gameId)));
        game.setBoard(userBoard);
        game.setStatus(userBoard.checkGameStatus());
        log.info("User makes a move in game {}, new status: {}", game.getId(), game.getStatus());
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            int[] agentMove = MinimaxAgent.getMove(game.getBoard());
            game.getBoard().getMatrix()[agentMove[0]][agentMove[1]] = GameConstant.PLAYER_O;
            game.setStatus(userBoard.checkGameStatus());
            log.info("Agent makes a move in game {}, new status: {}", game.getId(), game.getStatus());
        }
        gameRepository.saveGame(game);
        return game;
    }

    @Override
    public boolean validateUserBoard(UUID gameId, BoardModel userBoard) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() ->
                new GameNotFoundException(String.format("Invalid game id %s", gameId)));

        return isBoardValid(game.getBoard(), userBoard);
    }

    @Override
    public boolean isGameOver(UUID gameId) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() ->
                new GameNotFoundException(String.format("Invalid game id %s", gameId)));

        return game.getStatus() != GameStatus.IN_PROGRESS;
    }

    @Override
    public GameModel createNewGame() {
        GameModel game = new GameModel();
        gameRepository.saveGame(game);
        return game;
    }

    private boolean isBoardValid(BoardModel prev, BoardModel next) {
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