package com.trumpecy.tictactoe.domain.service.game;

import com.trumpecy.tictactoe.domain.model.game.GameStatus;
import org.junit.jupiter.api.Test;

import static com.trumpecy.tictactoe.domain.service.game.GameLogicUtility.checkGameStatus;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameLogicUtilityTest {
    @Test
    public void testHorizontalWinFirstRowX() {
        int[][] board = new int[][]{
                {1, 1, 1},
                {2, 0, 2},
                {0, 0, 0}
        };
        assertEquals(GameStatus.X_WINS, checkGameStatus(board));
    }

    @Test
    public void testHorizontalWinSecondRowO() {
        int[][] board = new int[][]{
                {1, 0, 1},
                {2, 2, 2},
                {0, 1, 0}
        };
        assertEquals(GameStatus.O_WINS, checkGameStatus(board));
    }

    @Test
    public void testVerticalWinFirstColumnX() {
        int[][] board = new int[][]{
                {1, 2, 0},
                {1, 2, 0},
                {1, 0, 0}
        };
        assertEquals(GameStatus.X_WINS, checkGameStatus(board));
    }

    @Test
    public void testVerticalWinThirdColumnO() {
        int[][] board = new int[][]{
                {0, 1, 2},
                {0, 1, 2},
                {1, 0, 2}
        };
        assertEquals(GameStatus.O_WINS, checkGameStatus(board));
    }

    @Test
    public void testMainDiagonalWinX() {
        int[][] board = new int[][]{
                {1, 2, 0},
                {0, 1, 2},
                {0, 0, 1}
        };
        assertEquals(GameStatus.X_WINS, checkGameStatus(board));
    }

    @Test
    public void testAntiDiagonalWinO() {
        int[][] board = new int[][]{
                {0, 1, 2},
                {1, 2, 0},
                {2, 0, 0}
        };

        assertEquals(GameStatus.O_WINS, checkGameStatus(board));
    }

    @Test
    public void testDraw() {
        int[][] board = new int[][]{
                {1, 2, 1},
                {2, 1, 1},
                {2, 1, 2}
        };

        assertEquals(GameStatus.DRAW, checkGameStatus(board));
    }

    @Test
    public void testInProgress() {
        int[][] board = new int[][]{
                {1, 0, 2},
                {0, 1, 0},
                {0, 0, 0}
        };

        assertEquals(GameStatus.IN_PROGRESS, checkGameStatus(board));
    }
}