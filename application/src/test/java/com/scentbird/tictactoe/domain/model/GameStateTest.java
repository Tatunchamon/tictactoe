package com.scentbird.tictactoe.domain.model;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void testConstructor_notEmptyBoard() {
        // GIVEN
        String id = "game1";
        Player myPlayer = Player.X;

        // WHEN
        GameState game = new GameState(id, myPlayer);

        // THEN
        assertFalse(game.getBoard().isEmpty(), "The board shouldn't be empty.");
        assertEquals(GameState.BOARD_HEIGHT * GameState.BOARD_WIDTH, game.getBoard().size(),
                "The board size should be BOARD_HEIGHT * BOARD_WIDTH.");
        assertTrue(game.getBoard().stream().allMatch(cv -> cv == GameState.CellValue.EMPTY),
                "All cells in the board should be GameState.CellValue.EMPTY.");
    }

    @Test
    void testConstructor_withCells_notEmptyBoard() {
        // GIVEN
        String id = "game2";
        Player myPlayer = Player.O;
        GameState.CellValue[] cells = new GameState.CellValue[]{GameState.CellValue.EMPTY, GameState.CellValue.O, GameState.CellValue.X};

        // WHEN
        GameState game = new GameState(id, myPlayer, Arrays.asList(cells));

        // THEN
        assertFalse(game.getBoard().isEmpty(), "The board shouldn't be empty.");
        assertEquals(cells.length, game.getBoard().size(), "The board size should be the same as the cells array's length.");
    }
}
