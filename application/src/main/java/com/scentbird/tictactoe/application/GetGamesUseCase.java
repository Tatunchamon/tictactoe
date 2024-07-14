package com.scentbird.tictactoe.application;

import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.repository.GameRepository;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GetGamesUseCase {
    private final GameRepository gameRepository;

    public GetGamesUseCase(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Map<String, GameState> getGame() {
        return gameRepository.findAll();
    }
}
