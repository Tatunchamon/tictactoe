package com.scentbird.tictactoe.controller;

import com.scentbird.tictactoe.application.GetGameUseCase;
import com.scentbird.tictactoe.application.GetGamesUseCase;
import com.scentbird.tictactoe.domain.model.GameState;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final GetGameUseCase getGameUseCase;
    private final GetGamesUseCase getGamesUseCase;

    @Autowired
    public GameController(GetGameUseCase getGameUseCase, GetGamesUseCase getGamesUseCase) {
        this.getGamesUseCase = getGamesUseCase;
        this.getGameUseCase = getGameUseCase;
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<GameState> getGame(@PathVariable String gameId) {
        return new ResponseEntity<>(getGameUseCase.getGame(gameId), HttpStatus.OK);
    }

    @GetMapping("/games")
    public ResponseEntity<Map<String, GameState>> getGame() {
        return new ResponseEntity<>(getGamesUseCase.getGame(), HttpStatus.OK);
    }
}
