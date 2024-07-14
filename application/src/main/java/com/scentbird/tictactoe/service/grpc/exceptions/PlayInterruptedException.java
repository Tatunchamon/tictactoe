package com.scentbird.tictactoe.service.grpc.exceptions;

public class PlayInterruptedException extends RuntimeException {
    public PlayInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
