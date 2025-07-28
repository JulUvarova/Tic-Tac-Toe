package com.trumpecy.tictactoe.domain.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {
    private UUID userId;
    private int wins;
    private int losses;
    private int draws;
    private float winRatio;
}
