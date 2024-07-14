package com.scentbird.tictactoe.exception;

public class TicTacToeRuntimeException extends RuntimeException {
    public TicTacToeRuntimeException(String message) {
        super(message);
    }
    public TicTacToeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
