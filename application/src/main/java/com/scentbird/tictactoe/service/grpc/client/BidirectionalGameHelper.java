package com.scentbird.tictactoe.service.grpc.client;

import com.scentbird.tictactoe.TicTacToeServiceGrpc;
import com.scentbird.tictactoe.TurnRequest;
import static com.scentbird.tictactoe.domain.model.GameStatus.IN_PROGRESS;
import com.scentbird.tictactoe.service.MoveHandler;
import com.scentbird.tictactoe.service.grpc.Converter;
import com.scentbird.tictactoe.service.grpc.exceptions.PlayInterruptedException;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BidirectionalGameHelper {
    private final CountDownLatch finishedLatch;
    private final MoveHandler moveHandler;
    private StreamObserver<TurnRequest> responseObserver;

    private final TicTacToeServiceGrpc.TicTacToeServiceStub serviceStub;

    public BidirectionalGameHelper(MoveHandler moveHandler, TicTacToeServiceGrpc.TicTacToeServiceStub serviceStub) {
        this.moveHandler = moveHandler;
        this.finishedLatch = new CountDownLatch(1);
        this.serviceStub = serviceStub;
    }

    public void start() {
        responseObserver = serviceStub.play(new PingPongClient(moveHandler));
    }

    public void await() {
        try {
            finishedLatch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PlayInterruptedException(e.getMessage(), e);
        }
    }

    public void onNext(TurnRequest makeMoveRequest) {
        responseObserver.onNext(makeMoveRequest);
    }

    private class PingPongClient implements StreamObserver<TurnRequest> {
        private static final Logger LOGGER = LoggerFactory.getLogger(PingPongClient.class);

        private final MoveHandler moveHandler;

        public PingPongClient(MoveHandler moveHandler) {
            this.moveHandler = moveHandler;
        }

        @Override
        public void onNext(TurnRequest request) {
            if (request.hasStatus()) {
                LOGGER.info("Accepting finished game: {}", request.getStatus());
                responseObserver.onCompleted();
                return;
            }
            var gameStatus = moveHandler.opponentTurn(request);

            if (gameStatus != IN_PROGRESS) {
                LOGGER.info("Game has finished: {}", gameStatus);
                TurnRequest response = TurnRequest.newBuilder()
                        .setGameId(request.getGameId())
                        .setStatus(Converter.gameStatus(gameStatus))
                        .build();
                responseObserver.onNext(response);
                return;
            }

            var myNextMove = moveHandler.myTurn(request.getGameId());

            TurnRequest response = TurnRequest.newBuilder()
                    .setGameId(request.getGameId())
                    .setMove(Converter.move(myNextMove))
                    .build();
            
            LOGGER.info("My turn is: {}", response);
            responseObserver.onNext(response);
        }

        @Override
        public void onError(Throwable t) {
            LOGGER.error("An error has occurred: {}", t.getMessage(), t);
        }

        @Override
        public void onCompleted() {
            LOGGER.info("Game was finished");
            finishedLatch.countDown();
        }
    }
}
