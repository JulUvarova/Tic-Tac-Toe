package com.trumpecy.tictactoe.domain.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRatio {
    private UUID userId;
    private double winRatio;
}
