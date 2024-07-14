package com.scentbird.tictactoe.domain.logic;

import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.domain.model.GameStatus;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameValidatorTest {

    private final GameValidator gameValidator = new GameValidator();

    @Test
    void testXWon() {
        // GIVEN
        GameState mockedGameState = mock(GameState.class);
        when(mockedGameState.getBoard()).thenReturn(Arrays.asList(
                GameState.CellValue.X, GameState.CellValue.X, GameState.CellValue.X,  // 1st row
                GameState.CellValue.EMPTY, GameState.CellValue.EMPTY, GameState.CellValue.EMPTY,  // 2nd row
                GameState.CellValue.EMPTY, GameState.CellValue.EMPTY, GameState.CellValue.EMPTY));  // 3rd row

        // WHEN
        gameValidator.checkGameStatus(mockedGameState);

        // THEN
        verify(mockedGameState, times(1)).setStatus(GameStatus.X_WON);
    }

    @Test
    void testOWon() {
        // GIVEN
        GameState mockedGameState = mock(GameState.class);
        when(mockedGameState.getBoard()).thenReturn(Arrays.asList(
                GameState.CellValue.O, GameState.CellValue.EMPTY, GameState.CellValue.EMPTY,  // 1st row
                GameState.CellValue.O, GameState.CellValue.EMPTY, GameState.CellValue.EMPTY,  // 2nd row
                GameState.CellValue.O, GameState.CellValue.EMPTY, GameState.CellValue.EMPTY));  // 3rd row

        // WHEN
        gameValidator.checkGameStatus(mockedGameState);

        // THEN
        verify(mockedGameState, times(1)).setStatus(GameStatus.O_WON);
    }

    @Test
    void testDraw() {
        // GIVEN
        GameState mockedGameState = mock(GameState.class);
        when(mockedGameState.getBoard()).thenReturn(Arrays.asList(
                GameState.CellValue.O, GameState.CellValue.X, GameState.CellValue.O,  // 1st row
                GameState.CellValue.X, GameState.CellValue.X, GameState.CellValue.O,  // 2nd row
                GameState.CellValue.O, GameState.CellValue.O, GameState.CellValue.X));  // 3rd row

        // WHEN
        gameValidator.checkGameStatus(mockedGameState);

        // THEN
        verify(mockedGameState, times(1)).setStatus(GameStatus.DRAW);
    }
}
