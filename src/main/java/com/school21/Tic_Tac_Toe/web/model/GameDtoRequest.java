package com.school21.Tic_Tac_Toe.web.model;

import com.school21.Tic_Tac_Toe.web.annotation.ValidBoard;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDtoRequest {
    @NotNull(message = "Game UUID can't be null")
    private UUID id;
    @NotNull(message = "Game board can't be null")
    @ValidBoard
    private int[][] board;
}