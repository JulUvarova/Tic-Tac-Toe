package com.school21.Tic_Tac_Toe.web.model;

import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDtoResponse {
    private UUID id;
    private int[][] board;
    private UUID playerX;
    private UUID playerO;
    private GameStatus status;
    private UUID currentPlayer;
}