package com.scentbird.tictactoe.service;

import com.scentbird.tictactoe.TurnRequest;
import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.domain.model.GameStatus;
import com.scentbird.tictactoe.domain.model.Move;
import java.util.Collection;

public interface MoveHandler {
    Move myTurn(String gameId);

    GameStatus opponentTurn(TurnRequest request);

    Move chooseCell(Collection<GameState.CellValue> board);
}
