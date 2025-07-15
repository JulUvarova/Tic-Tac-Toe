package com.school21.Tic_Tac_Toe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardModel {
    private int[][] matrix = new int[GameConstant.BOARD_SIDE][GameConstant.BOARD_SIDE];

    public BoardModel(BoardModel boardModel) {
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                this.matrix[i][j] = boardModel.getMatrix()[i][j];
            }
        }
    }

    public GameStatus checkGameStatus() {
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
}
