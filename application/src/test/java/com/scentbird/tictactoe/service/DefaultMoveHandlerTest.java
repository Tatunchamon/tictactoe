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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DefaultMoveHandlerTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameValidator gameValidator;

    @Mock
    private CoordinatesConvertor coordinatesConvertor;

    @InjectMocks
    private DefaultMoveHandler defaultMoveHandler;


    @Test
    void testChooseCell_withEmptyCells() {
        // GIVEN
        Collection<GameState.CellValue> board = Arrays.asList(
                GameState.CellValue.EMPTY,
                GameState.CellValue.X,
                GameState.CellValue.EMPTY,
                GameState.CellValue.O,
                GameState.CellValue.EMPTY
        );
        when(coordinatesConvertor.coordinatesOf(anyInt())).thenReturn(new Move(0, 0));

        // WHEN
        Move move = defaultMoveHandler.chooseCell(board);

        // THEN
        assertNotNull(move);
        verify(coordinatesConvertor, times(1)).coordinatesOf(anyInt());
    }

    @Test
    void testChooseCell_withNoEmptyCells() {
        // GIVEN
        Collection<GameState.CellValue> board = Arrays.asList(
                GameState.CellValue.X,
                GameState.CellValue.X,
                GameState.CellValue.O,
                GameState.CellValue.O
        );

        // WHEN & THEN
        assertThrows(TicTacToeRuntimeException.class, () -> {
            defaultMoveHandler.chooseCell(board);
        });
    }

    @Test
    void testMyTurn() {
        // GIVEN
        String gameId = UUID.randomUUID().toString();

        GameState gameState = new GameState(gameId, Player.X);
        Move move = new Move(0, 0);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameState));
        when(coordinatesConvertor.coordinatesOf(anyInt())).thenReturn(move);

        // WHEN
        Move resultMove = defaultMoveHandler.myTurn(gameId);

        // THEN
        assertEquals(move, resultMove);
        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, times(1)).save(gameState);
    }

    @Test
    void testOpponentTurn_statusIN_PROGRESS() {
        // GIVEN
        String gameId = UUID.randomUUID().toString();
        TurnRequest request = TurnRequest.newBuilder()
                .setMove(com.scentbird.tictactoe.Move.newBuilder().setY(0).setX(0).build())
                .setGameId(gameId)
                .build();
        GameState gameState = new GameState(gameId, Player.X);

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameState));

        // WHEN
        GameStatus status = defaultMoveHandler.opponentTurn(request);

        // THEN
        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, times(1)).save(gameState);
        assertEquals(GameStatus.IN_PROGRESS, status);
    }

    @Test
    void testMakeMove_withValidMove() {
        // GIVEN
        String gameId = UUID.randomUUID().toString();
        GameState gameState = new GameState(gameId, Player.X,
                List.of(GameState.CellValue.X,
                        GameState.CellValue.O,
                        GameState.CellValue.X,
                        GameState.CellValue.X,
                        GameState.CellValue.O,
                        GameState.CellValue.O,
                        GameState.CellValue.X,
                        GameState.CellValue.O,
                        GameState.CellValue.EMPTY
                ));

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameState));
        when(coordinatesConvertor.indexOf(any())).thenReturn(8);

        // WHEN
        defaultMoveHandler.myTurn(gameState.getId());

        // THEN
        verify(gameValidator, times(1)).checkGameStatus(gameState);
        verify(gameRepository, times(1)).save(gameState);
        verify(gameRepository, times(1)).findById(gameId);
    }

    @Test
    void testMakeMove_gameNotFound() {
        // GIVEN
        String gameId = UUID.randomUUID().toString();

        TurnRequest request = TurnRequest.newBuilder()
                .setMove(com.scentbird.tictactoe.Move.newBuilder().setY(0).setX(0).build())
                .setGameId(gameId)
                .build();

        // WHEN && THEN
        assertThrows(GameNotFoundException.class, () -> {
            defaultMoveHandler.myTurn(gameId);
        });
        assertThrows(GameNotFoundException.class, () -> {
            defaultMoveHandler.opponentTurn(request);
        });
    }

    @Test
    void testMakeMove_withInvalidMove() {
        // GIVEN
        String gameId = UUID.randomUUID().toString();
        GameState gameState = new GameState(gameId, Player.X,
                List.of(GameState.CellValue.X,
                        GameState.CellValue.O,
                        GameState.CellValue.X,
                        GameState.CellValue.X,
                        GameState.CellValue.O,
                        GameState.CellValue.O,
                        GameState.CellValue.X,
                        GameState.CellValue.O,
                        GameState.CellValue.EMPTY
                ));

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(gameState));
        when(coordinatesConvertor.indexOf(any())).thenReturn(1);

        // WHEN
        assertThrows(IllegalMoveException.class, () -> {
            defaultMoveHandler.myTurn(gameId);
        });

        // THEN
        verify(gameValidator, times(0)).checkGameStatus(any());
        verify(gameRepository, times(0)).save(any());
        verify(gameRepository, times(1)).findById(gameId);
    }
}
