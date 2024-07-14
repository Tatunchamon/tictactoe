package com.scentbird.tictactoe.domain.logic;

import com.scentbird.tictactoe.domain.model.GameState;
import static com.scentbird.tictactoe.domain.model.GameState.BOARD_HEIGHT;
import com.scentbird.tictactoe.domain.model.Move;
import org.springframework.stereotype.Component;

@Component
public class CoordinatesConvertor {

    public int indexOf(Move move) {
        return move.x() + move.y() * GameState.BOARD_WIDTH;
    }

    public Move coordinatesOf(int index) {
        int x = index % GameState.BOARD_WIDTH;
        int y = index / BOARD_HEIGHT;
        return new Move(x, y);
    }
}
