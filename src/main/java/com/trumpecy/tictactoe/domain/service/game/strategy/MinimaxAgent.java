package com.trumpecy.tictactoe.domain.service.game.strategy;

import com.trumpecy.tictactoe.domain.model.game.Board;
import com.trumpecy.tictactoe.domain.model.game.GameConstant;
import com.trumpecy.tictactoe.domain.model.game.GameStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MinimaxAgent {
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
        GameStatus status = board.checkGameStatus();
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
}
