package com.scentbird.tictactoe.service;

import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.exception.IllegalMoveException;
import com.scentbird.tictactoe.service.grpc.client.TicTacToeClient;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameProcessor.class);

    private final TicTacToeClient ticTacToeClient;
    private final ThreadPoolExecutor threadPoolExecutor;

    @Autowired
    public GameProcessor(TicTacToeClient ticTacToeClient, ThreadPoolExecutor threadPoolExecutor) {
        this.ticTacToeClient = ticTacToeClient;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void playGame(GameState game) {
        CompletableFuture.runAsync(() -> this.play(game), threadPoolExecutor);
    }

    public void play(GameState game) {
        try {
            ticTacToeClient.play(game);
        } catch (IllegalMoveException e) {
            LOGGER.error(e.getMessage(), e);
            // TODO: Add logic: as example retry
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
