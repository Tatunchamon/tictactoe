package com.scentbird.tictactoe.application;

import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.exception.StartGameException;
import com.scentbird.tictactoe.repository.GameRepository;
import com.scentbird.tictactoe.service.GameProcessor;
import com.scentbird.tictactoe.service.grpc.client.TicTacToeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class StartGameUseCase {
    private final TicTacToeClient ticTacToeClient;
    private final GameProcessor gameProcessor;
    private final GameRepository gameRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameUseCase.class);

    @Autowired
    public StartGameUseCase(@Lazy TicTacToeClient ticTacToeClient, GameProcessor gameProcessor, GameRepository gameRepository) {
        this.ticTacToeClient = ticTacToeClient;
        this.gameProcessor = gameProcessor;
        this.gameRepository = gameRepository;
    }

    public String startGame() {
        GameState game;
        try {
            game = ticTacToeClient.startNewGame();
            LOGGER.info("Game with id {} started. I am player {}", game.getId(), game.getMyPlayer());
            LOGGER.info("Board: {}", game.getBoard());
            gameRepository.save(game);
        } catch (Exception ex) {
            throw new StartGameException("Error starting a new game: " + ex.getMessage(), ex);
        }
        
        gameProcessor.playGame(game);

        return game.getId();
    }
}
