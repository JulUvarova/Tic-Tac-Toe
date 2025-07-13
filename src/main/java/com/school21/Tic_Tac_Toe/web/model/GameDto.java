package com.school21.Tic_Tac_Toe.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class GameDto {
    private UUID id;
    private int[][] board;
}