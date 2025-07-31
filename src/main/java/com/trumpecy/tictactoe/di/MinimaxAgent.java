package com.trumpecy.tictactoe.di;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
public class MinimaxAgent {
    private String name = "MinimaxAgent";
    private UUID id = UUID.fromString("00000000-0000-0000-0000-000000000001");
}
