package com.school21.Tic_Tac_Toe.domain.model.game;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class GameConstant {
    public static final int BOARD_SIDE = 3;
    public static final int PLAYER_X = 1; // player
    public static final int PLAYER_O = 2; // agent
    public static final UUID MINIMAX_AGENT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000001");
}
