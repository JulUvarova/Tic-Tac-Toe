package com.school21.Tic_Tac_Toe.domain.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {
//
//    @Mock
//    private GameRepository gameRepository;
//
//    @InjectMocks
//    private GameServiceImpl gameService;
//
//    private static Game game;
//    private Board nextBoard;
//
//    @BeforeAll
//    static void setUp() {
//        game = new Game();
//    }
//
//    @BeforeEach
//    void setUpGame() {
//        nextBoard = new Board();
//    }
//
//    @Test
//    void getNextMove_AgentMakesMove() {
//        Board userBoard = new Board(new int[][]{
//                {1, 0, 0},
//                {0, 0, 0},
//                {0, 0, 0}
//        });
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        Game result = gameService.getNextMove(game.getId(), userBoard);
//        Board resultBoard = result.getBoard();
//        int oCount = 0;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                if (resultBoard.getMatrix()[i][j] == GameConstant.PLAYER_O) {
//                    oCount++;
//                }
//            }
//        }
//        assertEquals(1, oCount);
//        verify(gameRepository).saveGame(game);
//    }
//
//    @Test
//    void getNextMove_AgentMakesMoveAndWin() {
//        Board userBoard = new Board(new int[][]{
//                {1, 0, 2},
//                {1, 0, 2},
//                {0, 0, 0}
//        });
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        Game result = gameService.getNextMove(game.getId(), userBoard);
//
//        assertEquals(GameStatus.O_WINS, result.getBoard().checkGameStatus());
//        verify(gameRepository).saveGame(game);
//    }
//
//    @Test
//    void getNextMove_AgentMakesMoveForDraw() {
//        Board userBoard = new Board(new int[][]{
//                {0, 1, 0},
//                {0, 2, 1},
//                {1, 2, 1}
//        });
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        Game result = gameService.getNextMove(game.getId(), userBoard);
//
//        assertEquals(GameConstant.PLAYER_O, result.getBoard().getMatrix()[0][2]);
//        assertEquals(GameStatus.IN_PROGRESS, result.getBoard().checkGameStatus());
//        verify(gameRepository).saveGame(game);
//    }
//
//    @Test
//    void getNextMove_GameOverAfterUserMove() {
//        Board userBoard = new Board(new int[][]{
//                {1, 1, 1},
//                {0, 0, 0},
//                {0, 0, 0}
//        });
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        Game result = gameService.getNextMove(game.getId(), userBoard);
//
//        assertEquals(GameStatus.X_WINS, result.getBoard().checkGameStatus());
//        assertArrayEquals(userBoard.getMatrix(), result.getBoard().getMatrix());
//    }
//
//    @Test
//    void getNextMove_InvalidGameId() {
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> gameService.getNextMove(game.getId(), new Board()));
//    }
//
//    @Test
//    public void testIsBoardValid_ValidMove() {
//        game.setBoard(new Board());
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_X;
//
//        assertTrue(gameService.validateUserBoard(game.getId(), nextBoard));
//    }
//
//    @Test
//    public void testIsBoardValid_Invalid_MultipleChanges() {
//        game.setBoard(new Board());
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_X;
//        nextBoard.getMatrix()[0][1] = GameConstant.PLAYER_X;
//
//        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));
//    }
//
//    @Test
//    public void testIsBoardValid_Invalid_ChangeOccupiedCell() {
//        game.setBoard(new Board());
//        game.getBoard().getMatrix()[0][0] = GameConstant.PLAYER_O;
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_X;
//
//        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));
//    }
//
//    @Test
//    public void testIsBoardValid_Invalid_ChangeToNotPlayerX() {
//        game.setBoard(new Board());
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        nextBoard.getMatrix()[0][0] = GameConstant.PLAYER_O;
//
//        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));
//    }
//
//    @Test
//    public void testIsBoardValid_Invalid_NullBoards() {
//        game.setBoard(null);
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        assertFalse(gameService.validateUserBoard(game.getId(), nextBoard));
//
//        game.setBoard(new Board());
//        assertFalse(gameService.validateUserBoard(game.getId(), null));
//    }
//
//    @Test
//    public void testIsGameOver_False() {
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        assertFalse(gameService.isGameOver(game.getId()));
//    }
//
//    @Test
//    public void testIsGameOver_True() {
//        game.setBoard(new Board(new int[][]{
//                {1, 1, 1},
//                {0, 0, 0},
//                {0, 0, 0}
//        }));
//        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
//
//        assertTrue(gameService.isGameOver(game.getId()));
//    }
}
