package com.scentbird.tictactoe.service.grpc.client;

import com.scentbird.tictactoe.NewGameRequest;
import com.scentbird.tictactoe.TicTacToeServiceGrpc;
import com.scentbird.tictactoe.TurnRequest;
import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.exception.IllegalMoveException;
import com.scentbird.tictactoe.service.MoveHandler;
import com.scentbird.tictactoe.service.grpc.Converter;
import io.grpc.StatusRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TicTacToeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeClient.class);

    private final TicTacToeServiceGrpc.TicTacToeServiceBlockingStub ticTacToeStub;

    private final TicTacToeServiceGrpc.TicTacToeServiceStub ticTacToeStubAsync;

    private final MoveHandler moveHandler;

    public TicTacToeClient(MoveHandler moveHandler,
                           TicTacToeServiceGrpc.TicTacToeServiceBlockingStub ticTacToeStub,
                           TicTacToeServiceGrpc.TicTacToeServiceStub ticTacToeStubAsync) {
        this.moveHandler = moveHandler;
        this.ticTacToeStub = ticTacToeStub;
        this.ticTacToeStubAsync = ticTacToeStubAsync;
    }

    public GameState startNewGame() {
        LOGGER.info("[CLIENT]: Creating a new game");
        NewGameRequest request = NewGameRequest.newBuilder().build();
        return Converter.game(ticTacToeStub.newGame(request));
    }

    public void play(GameState game) {
        try {
            var client = new BidirectionalGameHelper(moveHandler, this.ticTacToeStubAsync);
            var move = moveHandler.myTurn(game.getId());
            
            LOGGER.info("Starting to play. My first move is: {}", move);
            
            TurnRequest response = TurnRequest.newBuilder()
                    .setGameId(game.getId())
                    .setMove(Converter.move(move))
                    .build();

            client.start();
            client.onNext(response);
            client.await();
            
            LOGGER.info("Game over.");
        } catch (StatusRuntimeException e) {
            LOGGER.error(e.getMessage(), e);

            switch (e.getStatus().getCode()) {
                case INVALID_ARGUMENT -> throw new IllegalMoveException();
                default -> throw e;
            }
        }
    }
}
