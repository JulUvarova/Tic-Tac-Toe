package com.school21.Tic_Tac_Toe.datasource.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity(name = "games")
@Data
@NoArgsConstructor
public class GameEntity {
    @Id
    private UUID id;
    private String board;
}
