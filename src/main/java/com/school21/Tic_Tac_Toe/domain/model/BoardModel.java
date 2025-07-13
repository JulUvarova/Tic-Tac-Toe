package com.school21.Tic_Tac_Toe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardModel {
    private int[][] matrix = new int[GameConstant.BOARD_SIDE][GameConstant.BOARD_SIDE];
}
