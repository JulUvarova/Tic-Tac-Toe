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
            System.arraycopy(boardModel.getMatrix()[i], 0, this.matrix[i], 0, GameConstant.BOARD_SIDE);
        }
    }
}
