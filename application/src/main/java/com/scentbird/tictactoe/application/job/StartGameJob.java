package com.scentbird.tictactoe.application.job;

import com.scentbird.tictactoe.application.StartGameUseCase;
import com.scentbird.tictactoe.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StartGameJob {
    private final StartGameUseCase startGameUseCase;
    private final GameRepository gameRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(StartGameJob.class);

    public StartGameJob(StartGameUseCase startGameUseCase, GameRepository gameRepository) {
        this.startGameUseCase = startGameUseCase;
        this.gameRepository = gameRepository;
    }

    @Scheduled(
            // the random delay will be between 2 and 4 seconds
            initialDelayString = "#{new Double((T(java.lang.Math).random() + 1) * 2000).intValue()}",
            fixedDelayString = "${application.run-delay-ms}")
    public void invoke() {
        try {
            var game = gameRepository.findNotFinishGame();
            if (game.isPresent()) {
                LOGGER.debug("Game '{}' already started!\n{}", game.get().getId(), game.get());
                return;
            }

            var gameId = startGameUseCase.startGame();
            LOGGER.info("Game '{}' started!", gameId);
        } catch (Exception ex) {
            LOGGER.warn(ex.getMessage());
        }
    }

}
