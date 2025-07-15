package com.school21.Tic_Tac_Toe.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardModelTest {
    @Test
    public void testHorizontalWinFirstRowX() {
        BoardModel board = new BoardModel(new int[][]{
                {1, 1, 1},
                {2, 0, 2},
                {0, 0, 0}
        });
        assertEquals(GameStatus.X_WINS, board.checkGameStatus());
    }

    @Test
    public void testHorizontalWinSecondRowO() {
        BoardModel board = new BoardModel(new int[][]{
                {1, 0, 1},
                {2, 2, 2},
                {0, 1, 0}
        });
        assertEquals(GameStatus.O_WINS, board.checkGameStatus());
    }

    @Test
    public void testVerticalWinFirstColumnX() {
        BoardModel board = new BoardModel(new int[][]{
                {1, 2, 0},
                {1, 2, 0},
                {1, 0, 0}
        });
        assertEquals(GameStatus.X_WINS, board.checkGameStatus());
    }

    @Test
    public void testVerticalWinThirdColumnO() {
        BoardModel board = new BoardModel(new int[][]{
                {0, 1, 2},
                {0, 1, 2},
                {1, 0, 2}
        });
        assertEquals(GameStatus.O_WINS, board.checkGameStatus());
    }

    @Test
    public void testMainDiagonalWinX() {
        BoardModel board = new BoardModel(new int[][]{
                {1, 2, 0},
                {0, 1, 2},
                {0, 0, 1}
        });
        assertEquals(GameStatus.X_WINS, board.checkGameStatus());
    }

    @Test
    public void testAntiDiagonalWinO() {
        BoardModel board = new BoardModel(new int[][]{
                {0, 1, 2},
                {1, 2, 0},
                {2, 0, 0}
        });
        assertEquals(GameStatus.O_WINS, board.checkGameStatus());
    }

    @Test
    public void testDraw() {
        BoardModel board = new BoardModel(new int[][]{
                {1, 2, 1},
                {2, 1, 1},
                {2, 1, 2}
        });
        assertEquals(GameStatus.DRAW, board.checkGameStatus());
    }

    @Test
    public void testInProgress() {
        BoardModel board = new BoardModel(new int[][]{
                {1, 0, 2},
                {0, 1, 0},
                {0, 0, 0}
        });
        assertEquals(GameStatus.IN_PROGRESS, board.checkGameStatus());
    }
}