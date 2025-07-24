package com.school21.Tic_Tac_Toe.datasource.model;

import java.util.UUID;

public interface UserRatioProjection {
    UUID getUserId();
    Double getWinRatio();
}
