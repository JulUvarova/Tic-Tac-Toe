package com.trumpecy.tictactoe.domain.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {
    private UUID id = UUID.randomUUID();
    private Board board = new Board();

    private UUID playerX;
    private UUID playerO;

    private GameStatus status;
    private UUID currentPlayer;

    private Instant startTime = Instant.now();

    public void updateStatus() {
        status = board.checkGameStatus();
    }
}
