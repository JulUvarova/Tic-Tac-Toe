package com.school21.Tic_Tac_Toe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameModel {
    private UUID id = UUID.randomUUID();
    private BoardModel board = new BoardModel();
}
