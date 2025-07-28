package com.trumpecy.tictactoe.web.model.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRatioDtoResponse {
    private UUID userId;
    private double winRatio;
}