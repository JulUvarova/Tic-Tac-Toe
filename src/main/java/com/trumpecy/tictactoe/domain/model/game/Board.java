package com.trumpecy.tictactoe.domain.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    private int[][] matrix = new int[GameConstant.BOARD_SIDE][GameConstant.BOARD_SIDE];

    public Board(Board boardModel) {
        for (int i = 0; i < GameConstant.BOARD_SIDE; i++) {
            for (int j = 0; j < GameConstant.BOARD_SIDE; j++) {
                this.matrix[i][j] = boardModel.getMatrix()[i][j];
            }
        }
    }
}
