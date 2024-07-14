package com.scentbird.tictactoe.application;

import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.exception.GameNotFoundException;
import com.scentbird.tictactoe.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GetGameUseCase {
    private final GameRepository gameRepository;

    public GetGameUseCase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public GameState getGame(String gameId) {
        return gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
    }
}
