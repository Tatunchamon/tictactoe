package com.scentbird.tictactoe.service.grpc.server;

import com.scentbird.tictactoe.NewGameRequest;
import com.scentbird.tictactoe.NewGameResponse;
import com.scentbird.tictactoe.TicTacToeServiceGrpc;
import com.scentbird.tictactoe.TurnRequest;
import com.scentbird.tictactoe.domain.model.GameState;
import static com.scentbird.tictactoe.domain.model.GameStatus.IN_PROGRESS;
import com.scentbird.tictactoe.domain.model.Player;
import com.scentbird.tictactoe.repository.GameRepository;
import com.scentbird.tictactoe.service.MoveHandler;
import com.scentbird.tictactoe.service.grpc.Converter;
import io.grpc.stub.StreamObserver;
import java.util.UUID;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class TicTacToeServer extends TicTacToeServiceGrpc.TicTacToeServiceImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicTacToeServer.class);

    private final GameRepository gameRepository;
    private final MoveHandler moveHandler;

    @Autowired
    public TicTacToeServer(GameRepository gameRepository, MoveHandler moveHandler) {
        this.gameRepository = gameRepository;
        this.moveHandler = moveHandler;
    }

    @Override
    public void newGame(NewGameRequest request, StreamObserver<NewGameResponse> responseObserver) {
        GameState game = gameRepository.findNotFinishGame().orElseGet(() -> {
            LOGGER.info("[SERVER]: Active games are not found. Creating a new game");
            String gameId = UUID.randomUUID().toString();
            var myPlayer = Player.getRandomPlayer();
            return new GameState(gameId, myPlayer);
        });

        gameRepository.save(game);

        var opponent = Player.another(game.getMyPlayer());

        NewGameResponse response = NewGameResponse.newBuilder()
                .setGameId(game.getId())
                .setPlayer(Converter.player(opponent))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<TurnRequest> play(StreamObserver<TurnRequest> responseObserver) {
        return new StreamObserver<>() {
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
                responseObserver.onCompleted();
            }
        };
    }
}
