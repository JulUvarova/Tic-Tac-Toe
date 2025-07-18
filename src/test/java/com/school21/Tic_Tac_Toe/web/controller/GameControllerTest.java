package com.school21.Tic_Tac_Toe.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school21.Tic_Tac_Toe.domain.model.game.Board;
import com.school21.Tic_Tac_Toe.domain.model.game.Game;
import com.school21.Tic_Tac_Toe.domain.service.game.GameService;
import com.school21.Tic_Tac_Toe.web.model.GameDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GameController.class)
class GameControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private GameService gameService;
    @Autowired
    private ObjectMapper objectMapper;
//
//    @Test
//    void createNewGame_Success() throws Exception {
//        Game game = new Game();
//        Mockito.when(gameService.createNewGame()).thenReturn(game);
//        mockMvc.perform(post("/game"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(game.getId().toString()));
//    }
//
//    @Test
//    void makeMove_Success() throws Exception {
//        Game game = new Game();
//        UUID id = game.getId();
//        Mockito.when(gameService.isGameOver(eq(id))).thenReturn(false);
//        Mockito.when(gameService.validateUserBoard(eq(id), any())).thenReturn(true);
//        Mockito.when(gameService.getNextMove(eq(id), any())).thenReturn(game);
//        GameDto dto = new GameDto();
//        dto.setId(id);
//        dto.setBoard(game.getBoard().getMatrix());
//        mockMvc.perform(post("/game/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id.toString()));
//    }
//
//    @Test
//    void makeMoveAfterGameOver_BadRequest() throws Exception {
//        UUID id = UUID.randomUUID();
//        Board board = new Board();
//        GameDto dto = new GameDto();
//        dto.setId(id);
//        dto.setBoard(board.getMatrix());
//        Mockito.when(gameService.isGameOver(eq(id))).thenReturn(true);
//        mockMvc.perform(post("/game/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void makeMoveInvalidId() throws Exception {
//        UUID id = UUID.randomUUID();
//        Board board = new Board();
//        GameDto dto = new GameDto();
//        dto.setId(UUID.randomUUID()); // не совпадает с id в path
//        dto.setBoard(board.getMatrix());
//        Mockito.when(gameService.isGameOver(any())).thenReturn(false);
//        mockMvc.perform(post("/game/" + id)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto)))
//                .andExpect(status().isBadRequest());
//    }
}
