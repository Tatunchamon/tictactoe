package com.scentbird.tictactoe.service.grpc;

import com.scentbird.tictactoe.Board;
import com.scentbird.tictactoe.domain.model.GameState;
import com.scentbird.tictactoe.domain.model.GameStatus;
import com.scentbird.tictactoe.domain.model.Move;
import com.scentbird.tictactoe.domain.model.Player;
import com.scentbird.tictactoe.service.grpc.exceptions.IllegalEnumValueException;
import java.util.Collection;

// todo use mapstruct or something else for this dumb operations
public class Converter {
    private Converter() {
    }

    public static Move move(com.scentbird.tictactoe.Move move) {
        return new Move(move.getX(), move.getY());
    }

    public static com.scentbird.tictactoe.Move move(Move move) {
        return com.scentbird.tictactoe.Move.newBuilder().setX(move.x()).setY(move.y()).build();
    }

    public static com.scentbird.tictactoe.Player player(Player player) {
        return switch (player) {
            case X -> com.scentbird.tictactoe.Player.PLAYER_X;
            case O -> com.scentbird.tictactoe.Player.PLAYER_O;
        };
    }

    public static Player player(com.scentbird.tictactoe.Player player) {
        return switch (player) {
            case PLAYER_X -> Player.X;
            case PLAYER_O -> Player.O;
            case PLAYER_UNSPECIFIED -> throw new IllegalEnumValueException("Unspecified Player value");
            case UNRECOGNIZED -> throw new IllegalEnumValueException("Unsupported Player value");
        };
    }

    public static com.scentbird.tictactoe.Board board(Collection<GameState.CellValue> cellValues) {
        var board = com.scentbird.tictactoe.Board.newBuilder();
        board.addAllCells(cellValues.stream().map(cell -> switch (cell) {
            case EMPTY -> Board.Cell.CELL_EMPTY;
            case X -> Board.Cell.CELL_X;
            case O -> Board.Cell.CELL_O;
        }).toList());
        return board.build();
    }

    public static Collection<GameState.CellValue> cellValues(com.scentbird.tictactoe.Board board) {
        return board.getCellsList().stream().map(cell -> switch (cell) {
            case CELL_EMPTY -> GameState.CellValue.EMPTY;
            case CELL_X -> GameState.CellValue.X;
            case CELL_O -> GameState.CellValue.O;
            default -> throw new IllegalEnumValueException("Unexpected value: " + cell);
        }).toList();
    }

    public static com.scentbird.tictactoe.GameStatus gameStatus(GameStatus gameStatus) {
        return switch (gameStatus) {
            case IN_PROGRESS -> com.scentbird.tictactoe.GameStatus.IN_PROGRESS;
            case DRAW -> com.scentbird.tictactoe.GameStatus.DRAW;
            case X_WON -> com.scentbird.tictactoe.GameStatus.X_WON;
            case O_WON -> com.scentbird.tictactoe.GameStatus.O_WON;
        };
    }


    public static GameState game(com.scentbird.tictactoe.NewGameResponse newGame) {
        return new GameState(newGame.getGameId(), Converter.player(newGame.getPlayer()), Converter.cellValues(newGame.getBoard()));
    }
}
