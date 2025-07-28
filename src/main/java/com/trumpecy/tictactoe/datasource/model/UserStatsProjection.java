package com.trumpecy.tictactoe.datasource.model;

import java.util.UUID;

public interface UserStatsProjection {
    UUID getUserId();
    Integer getWins();
    Integer getLosses();
    Integer getDraws();
}
