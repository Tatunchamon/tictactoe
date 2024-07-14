package com.scentbird.tictactoe.service;

import com.scentbird.tictactoe.TurnRequest;
import com.scentbird.tictactoe.domain.logic.CoordinatesConvertor;
import com.scentbird.tictactoe.domain.logic.GameValidator;
import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.domain.model.GameStatus;
import com.scentbird.tictactoe.domain.model.Move;
import com.scentbird.tictactoe.domain.model.Player;
import com.scentbird.tictactoe.exception.GameNotFoundException;
import com.scentbird.tictactoe.exception.IllegalMoveException;
import com.scentbird.tictactoe.exception.TicTacToeRuntimeException;
import com.scentbird.tictactoe.repository.GameRepository;
import com.scentbird.tictactoe.service.grpc.Converter;
import com.scentbird.tictactoe.service.grpc.exceptions.PlayInterruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DefaultMoveHandler implements MoveHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMoveHandler.class);
    private static final Random RANDOM = new Random();
    private final GameRepository gameRepository;
    private final GameValidator gameValidator;
    private final CoordinatesConvertor coordinatesConvertor;

    @Value("${application.response-delay-ms}")
    private int delayMs;

    public DefaultMoveHandler(GameRepository gameRepository,
                              GameValidator gameValidator,
                              CoordinatesConvertor coordinatesConvertor) {
        this.gameRepository = gameRepository;
        this.gameValidator = gameValidator;
        this.coordinatesConvertor = coordinatesConvertor;
    }

    @Override
    public Move chooseCell(Collection<GameState.CellValue> board) {
        LOGGER.info("Choose cell....");
        delay();
        List<Integer> emptyCells = new ArrayList<>();

        int i = 0;
        for (GameState.CellValue cell : board) {
            if (cell.isEmptyCell()) {
                emptyCells.add(i);
            }
            i++;
        }

        if (emptyCells.isEmpty()) {
            throw new TicTacToeRuntimeException("There are no empty cells on the board");
        }

        int position = emptyCells.get(RANDOM.nextInt(emptyCells.size()));
        LOGGER.info("Position {} was chosen for a new move", position);

        return coordinatesConvertor.coordinatesOf(position);
    }

    private void delay() {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PlayInterruptedException(e.getMessage(), e);
        }
    }

    public Move myTurn(String gameId) {
        var game = gameRepository.findById(gameId).orElseThrow(GameNotFoundException::new);
        var move = chooseCell(game.getBoard());
        makeMove(game, game.getMyPlayer(), move);

        gameRepository.save(game);

        return move;
    }

    public GameStatus opponentTurn(TurnRequest request) {
        LOGGER.info("Get request {}", request);
        var game = gameRepository.findById(request.getGameId()).orElseThrow(GameNotFoundException::new);
        makeMove(game, Player.another(game.getMyPlayer()), Converter.move(request.getMove()));
        LOGGER.info("Save move opponent user: {}", game);
        gameRepository.save(game);
        return game.getStatus();
    }

    private void makeMove(GameState gameState, Player player, Move move) {
        LOGGER.debug("before: {}, move: {}", gameState, move);
        int idx = coordinatesConvertor.indexOf(move);

        if (GameState.CellValue.EMPTY == gameState.getBoard().get(idx)) {
            gameState.getBoard().set(idx, GameState.CellValue.forPlayer(player));
            gameValidator.checkGameStatus(gameState);
            LOGGER.debug("after: {}", gameState);
            return;
        }
        throw new IllegalMoveException("Illegal move: " + move);
    }

}
