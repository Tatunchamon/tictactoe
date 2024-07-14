package com.scentbird.tictactoe.domain.logic;

import static com.scentbird.tictactoe.domain.model.GameState.BOARD_HEIGHT;
import static com.scentbird.tictactoe.domain.model.GameState.BOARD_WIDTH;
import com.scentbird.tictactoe.domain.model.Move;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class CoordinatesConvertorTest {

    private final CoordinatesConvertor coordinatesConvertor = new CoordinatesConvertor();

    @Test
    void testIndexOf() {
        // GIVEN
        var x = 2;
        var y = 3;
        var expectedIndex = x + y * BOARD_WIDTH;
        var move = new Move(x, y);

        // WHEN
        var result = coordinatesConvertor.indexOf(move);

        // THEN
        assertEquals(expectedIndex, result, "The index returned by indexOf did not match the expected result.");
    }

    @Test
    void testCoordinatesOf() {
        // GIVEN
        var index = 8;
        var expectedX = index % BOARD_WIDTH;
        var expectedY = index / BOARD_HEIGHT;
        var expectedMove = new Move(expectedX, expectedY);

        // WHEN
        Move result = coordinatesConvertor.coordinatesOf(index);

        // THEN
        assertEquals(expectedMove, result, "The coordinates returned by coordinatesOf did not match the expected result.");
    }
}
