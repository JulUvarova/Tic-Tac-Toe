package com.school21.Tic_Tac_Toe.datasource.model;

import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "games")
@Data
@NoArgsConstructor
public class GameEntity {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String board;
    @Column(nullable = false)
    private UUID playerX;
    private UUID playerO;
    private UUID currentPlayer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatus status;
}
