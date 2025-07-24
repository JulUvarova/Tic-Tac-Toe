package com.school21.Tic_Tac_Toe.datasource.model;

import java.util.UUID;

public interface UserStatsProjection {
    UUID getUserId();
    Integer getWins();
    Integer getLosses();
    Integer getDraws();
}
