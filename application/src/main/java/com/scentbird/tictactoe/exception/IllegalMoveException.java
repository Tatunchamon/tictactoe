package com.scentbird.tictactoe.exception;

public class IllegalMoveException extends TicTacToeRuntimeException {
    public IllegalMoveException(String msg) {
        super(msg);
    }
    public IllegalMoveException() {
        super("Illegal move");
    }
}
