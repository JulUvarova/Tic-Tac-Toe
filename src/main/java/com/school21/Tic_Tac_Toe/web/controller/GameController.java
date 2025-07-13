package com.school21.Tic_Tac_Toe.web.controller;

import com.school21.Tic_Tac_Toe.domain.model.GameModel;
import com.school21.Tic_Tac_Toe.domain.service.GameService;
import com.school21.Tic_Tac_Toe.web.mapper.GameWebMapper;
import com.school21.Tic_Tac_Toe.web.model.GameDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    /**
     * Implement a controller using Spring that has a POST /game/{UUID} method (where {UUID} is the UUID of the current game).
     * This method receives the current game with an updated board from the user and returns the current game with the updated board for the computer's turn.
     * If an invalid current game or updated board is sent, the method should return an error with a description.
     */
    @PostMapping("/{id}")
    public ResponseEntity<GameDto> makeMove(@PathVariable UUID id,
                                            @RequestBody GameDto moveRequest) {
        GameModel userMove = GameWebMapper.toGameModel(moveRequest);
        // валидация
        if (!id.equals(userMove.getId())) {
            return ResponseEntity.badRequest()
                    .body("Game id doesn't match!")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (!gameService.validateUserBoard(id, userMove.getBoard())) {
            return ResponseEntity.badRequest()
                    .body("Invalid move!")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        // обработка хода
        GameModel gameResponse = gameService.getNextMove(id, userMove.getBoard());
        return ResponseEntity.ok()
                .header("Content-type", "application/json")
                .body(GameWebMapper.toGameDto(gameResponse));
    }

    /**
     * Create new game
     */
    @PostMapping
    public ResponseEntity<GameDto> createNewGame() {
        GameModel newGameModel = gameService.createNewGame();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newGameModel.getId())
                .toUri();
        return ResponseEntity.created(location)
                .header("Content-Type", "application/json")
                .body(GameWebMapper.toGameDto(newGameModel));
    }
}
