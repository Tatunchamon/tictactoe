package com.scentbird.tictactoe.exception;

public class GameNotFoundException extends TicTacToeRuntimeException {
    public GameNotFoundException() {
        super("Game Not Found");
    }
}
