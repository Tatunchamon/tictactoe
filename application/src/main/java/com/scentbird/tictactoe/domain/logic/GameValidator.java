package com.scentbird.tictactoe.domain.logic;

import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.domain.model.GameStatus;
import org.springframework.stereotype.Component;

@Component
public class GameValidator {

    private static final int[][] WINNING_COMBINATIONS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // horizontal
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // vertical
            {0, 4, 8}, {2, 4, 6}  // diagonal
    };

    public void checkGameStatus(GameState gameState) {
        // Логика проверки статуса игры
        for (int[] combination : WINNING_COMBINATIONS) {
            var firstCell = gameState.getBoard().get(combination[0]);
            var secondCell = gameState.getBoard().get(combination[1]);
            var thirdCell = gameState.getBoard().get(combination[2]);
            if (!firstCell.isEmptyCell() && firstCell.equals(secondCell) && secondCell.equals(thirdCell)) {
                if (firstCell == GameState.CellValue.X) {
                    gameState.setStatus(GameStatus.X_WON);
                } else {
                    gameState.setStatus(GameStatus.O_WON);
                }
                return;
            }
        }

        if (gameState.getBoard().stream().noneMatch(GameState.CellValue::isEmptyCell)) {
            gameState.setStatus(GameStatus.DRAW);
        }
    }
}
