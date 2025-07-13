package com.school21.Tic_Tac_Toe.domain.service;

import com.school21.Tic_Tac_Toe.datasource.repository.GameRepository;
import com.school21.Tic_Tac_Toe.domain.model.BoardModel;
import com.school21.Tic_Tac_Toe.domain.model.GameConstant;
import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import com.school21.Tic_Tac_Toe.domain.model.GameStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public GameModel getNextMove(UUID gameId, BoardModel userBoard) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException());
        game.setBoard(userBoard);
        game.setStatus(checkGameStatus(userBoard));
        if (game.getStatus() == GameStatus.IN_PROGRESS) {
            getNextMoveByMiniMax(game.getBoard(), GameConstant.PLAYER_X);
            game.setStatus(checkGameStatus(game.getBoard()));
        }
        gameRepository.saveGame(game);
        return game;
    }

    @Override
    public boolean validateUserBoard(UUID gameId, BoardModel userBoard) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException());

        return isBoardValid(game.getBoard(), userBoard);
    }

    @Override
    public boolean isGameOver(UUID gameId) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException());

        return game.getStatus() == GameStatus.IN_PROGRESS ? false : true;
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
                    if (prev.getMatrix()[i][j] != 0 || next.getMatrix()[i][j] != GameConstant.PLAYER_X || count > 0) {
                        return false;
                    }
                    count++;
                }
            }
        }

        return true;
    }

    private void getNextMoveByMiniMax(BoardModel userBoard, int user) {
//       int []

    }

    private GameStatus checkGameStatus(BoardModel board) {
        // horizontal
        for (int i = 0; i < GameConstant.BOARD_SIDE && board.getMatrix()[i][0] != 0; i++) {
            if (board.getMatrix()[i][0] == board.getMatrix()[i][1] && board.getMatrix()[i][0] == board.getMatrix()[i][2]) {
                return board.getMatrix()[i][0] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
            }
        }
        // vertical
        for (int i = 0; i < GameConstant.BOARD_SIDE && board.getMatrix()[0][i] != 0; i++) {
            if (board.getMatrix()[0][i] == board.getMatrix()[1][i] && board.getMatrix()[0][i] == board.getMatrix()[2][i]) {
                return board.getMatrix()[0][i] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
            }
        }
        // diagonal
        if (board.getMatrix()[0][0] != 0 && board.getMatrix()[0][0] == board.getMatrix()[1][1] && board.getMatrix()[0][0] == board.getMatrix()[2][2]) {
            return board.getMatrix()[0][0] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
        }
        if (board.getMatrix()[0][2] != 0 && board.getMatrix()[0][2] == board.getMatrix()[1][1] && board.getMatrix()[0][2] == board.getMatrix()[2][0]) {
            return board.getMatrix()[0][2] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
        }
        // draw
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                if (board.getMatrix()[i][j] == 0) {
                    return GameStatus.IN_PROGRESS;
                }
            }
        }
        return GameStatus.DRAW;
    }
}
