package com.school21.Tic_Tac_Toe.datasource.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class GameEntity {
    private UUID id;
    private int[][] board;
}
