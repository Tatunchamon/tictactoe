package com.scentbird.tictactoe.repository;

import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.domain.model.GameStatus;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {
    private final Map<String, GameState> games = new HashMap<>();

    public void save(GameState game) {
        games.put(game.getId(), game);
    }

    public Optional<GameState> findById(String id) {
        return Optional.ofNullable(games.get(id));
    }

    public Map<String, GameState> findAll() {
        return games;
    }

    public Optional<GameState> findNotFinishGame() {
        return games.values().stream().filter(game -> game.getStatus().equals(GameStatus.IN_PROGRESS)).findFirst();
    }
}
