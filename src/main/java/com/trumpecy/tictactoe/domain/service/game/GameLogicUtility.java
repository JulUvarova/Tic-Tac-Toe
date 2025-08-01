package com.trumpecy.tictactoe.domain.service.game;

import com.trumpecy.tictactoe.domain.model.game.Board;
import com.trumpecy.tictactoe.domain.model.game.GameConstant;
import com.trumpecy.tictactoe.domain.model.game.GameStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameLogicUtility {
    public static GameStatus checkGameStatus(int[][] matrix) {
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            // horizontal
            if (matrix[i][0] != 0
                    && matrix[i][0] == matrix[i][1]
                    && matrix[i][0] == matrix[i][2]) {
                return matrix[i][0] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
            }
            // vertical
            if (matrix[0][i] != 0
                    && matrix[0][i] == matrix[1][i]
                    && matrix[0][i] == matrix[2][i]) {
                return matrix[0][i] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
            }
        }
        // diagonal
        if (matrix[0][0] != 0
                && matrix[0][0] == matrix[1][1]
                && matrix[0][0] == matrix[2][2]) {
            return matrix[0][0] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
        }
        if (matrix[0][2] != 0
                && matrix[0][2] == matrix[1][1]
                && matrix[0][2] == matrix[2][0]) {
            return matrix[0][2] == GameConstant.PLAYER_X ? GameStatus.X_WINS : GameStatus.O_WINS;
        }
        // draw
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                if (matrix[i][j] == 0) {
                    return GameStatus.IN_PROGRESS;
                }
            }
        }
        return GameStatus.DRAW;
    }

    public static int[] getMove(Board userBoard) {
        int[] agentBestMove = null;
        int bestScore = Integer.MAX_VALUE;
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                if (userBoard.getMatrix()[i][j] == 0) {
                    Board nextStepBoard = new Board(userBoard);
                    nextStepBoard.getMatrix()[i][j] = GameConstant.PLAYER_O;

                    int score = minimax(nextStepBoard, 0, true);

                    if (score < bestScore) {
                        bestScore = score;
                        agentBestMove = new int[]{i, j};
                    }
                }
            }
        }
        return agentBestMove;
    }

    private int minimax(Board board, int depth, boolean isMaximizing) {
        GameStatus status = checkGameStatus(board.getMatrix());
        if (status != GameStatus.IN_PROGRESS) {
            switch (status) {
                case DRAW -> {
                    return 0;
                }
                case X_WINS -> {
                    return 10 - depth;
                }
                case O_WINS -> {
                    return -10 + depth;
                }
            }
        }

        if (isMaximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
                for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                    if (board.getMatrix()[i][j] == 0) {
                        Board nextStepBoard = new Board(board);
                        nextStepBoard.getMatrix()[i][j] = GameConstant.PLAYER_X;
                        int score = minimax(nextStepBoard, depth + 1, false);
                        maxScore = Math.max(maxScore, score);
                    }
                }
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
                for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                    if (board.getMatrix()[i][j] == 0) {
                        Board nextStepBoard = new Board(board);
                        nextStepBoard.getMatrix()[i][j] = GameConstant.PLAYER_O;
                        int score = minimax(nextStepBoard, depth + 1, true);
                        minScore = Math.min(minScore, score);
                    }
                }
            }
            return minScore;
        }
    }

    public static boolean isBoardValid(int[][] prev, int[][] next, int player) {
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
