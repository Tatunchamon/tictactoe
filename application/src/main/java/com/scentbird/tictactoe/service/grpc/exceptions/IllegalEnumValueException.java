package com.scentbird.tictactoe.service.grpc.exceptions;

import com.scentbird.tictactoe.exception.TicTacToeRuntimeException;

public class IllegalEnumValueException extends TicTacToeRuntimeException {
    public IllegalEnumValueException(String message) {
        super(message);
    }
}
