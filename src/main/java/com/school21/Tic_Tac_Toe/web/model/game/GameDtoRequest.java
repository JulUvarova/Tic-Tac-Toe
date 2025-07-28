package com.school21.Tic_Tac_Toe.web.model.game;

import com.school21.Tic_Tac_Toe.web.annotation.ValidBoard;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDtoRequest {
    @NotNull(message = "Game board can't be null")
    @ValidBoard
    private int[][] board;
}