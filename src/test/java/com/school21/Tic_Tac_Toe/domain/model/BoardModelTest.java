package com.school21.Tic_Tac_Toe.domain.model;

import com.school21.Tic_Tac_Toe.domain.model.game.Board;
import com.school21.Tic_Tac_Toe.domain.model.game.GameStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardModelTest {
    @Test
    public void testHorizontalWinFirstRowX() {
        Board board = new Board(new int[][]{
                {1, 1, 1},
                {2, 0, 2},
                {0, 0, 0}
        });
        assertEquals(GameStatus.X_WINS, board.checkGameStatus());
    }

    @Test
    public void testHorizontalWinSecondRowO() {
        Board board = new Board(new int[][]{
                {1, 0, 1},
                {2, 2, 2},
                {0, 1, 0}
        });
        assertEquals(GameStatus.O_WINS, board.checkGameStatus());
    }

    @Test
    public void testVerticalWinFirstColumnX() {
        Board board = new Board(new int[][]{
                {1, 2, 0},
                {1, 2, 0},
                {1, 0, 0}
        });
        assertEquals(GameStatus.X_WINS, board.checkGameStatus());
    }

    @Test
    public void testVerticalWinThirdColumnO() {
        Board board = new Board(new int[][]{
                {0, 1, 2},
                {0, 1, 2},
                {1, 0, 2}
        });
        assertEquals(GameStatus.O_WINS, board.checkGameStatus());
    }

    @Test
    public void testMainDiagonalWinX() {
        Board board = new Board(new int[][]{
                {1, 2, 0},
                {0, 1, 2},
                {0, 0, 1}
        });
        assertEquals(GameStatus.X_WINS, board.checkGameStatus());
    }

    @Test
    public void testAntiDiagonalWinO() {
        Board board = new Board(new int[][]{
                {0, 1, 2},
                {1, 2, 0},
                {2, 0, 0}
        });
        assertEquals(GameStatus.O_WINS, board.checkGameStatus());
    }

    @Test
    public void testDraw() {
        Board board = new Board(new int[][]{
                {1, 2, 1},
                {2, 1, 1},
                {2, 1, 2}
        });
        assertEquals(GameStatus.DRAW, board.checkGameStatus());
    }

    @Test
    public void testInProgress() {
        Board board = new Board(new int[][]{
                {1, 0, 2},
                {0, 1, 0},
                {0, 0, 0}
        });
        assertEquals(GameStatus.IN_PROGRESS, board.checkGameStatus());
    }
}