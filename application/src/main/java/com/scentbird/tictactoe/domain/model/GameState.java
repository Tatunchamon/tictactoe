package com.scentbird.tictactoe.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Data;


@Data
public class GameState {
    public static final int BOARD_HEIGHT = 3;
    public static final int BOARD_WIDTH = 3;

    public enum CellValue {
        EMPTY, X, O;

        public static CellValue forPlayer(Player player) {
            return switch (player) {
                case X -> X;
                case O -> O;
            };
        }

        public boolean isEmptyCell() {
            return this == EMPTY;
        }
    }

    private final String id;
    private final List<CellValue> board;
    private GameStatus status;
    private Player myPlayer;

    public GameState(String id, Player myPlayer) {
        this.id = id;
        this.status = GameStatus.IN_PROGRESS;
        this.board = new ArrayList<>();
        this.myPlayer = myPlayer;
        fillEmptyBoard(board);
    }

    public GameState(String id, Player myPlayer, Collection<CellValue> cells) {
        this.id = id;
        this.board = new ArrayList<>(cells);
        this.status = GameStatus.IN_PROGRESS;
        this.myPlayer = myPlayer;
        if (board.isEmpty()) {
            fillEmptyBoard(board);
        }
    }

    private void fillEmptyBoard(List<GameState.CellValue> board) {
        IntStream.range(0, BOARD_HEIGHT * BOARD_WIDTH).forEach(ignored -> board.add(GameState.CellValue.EMPTY));
    }
}
