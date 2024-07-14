package com.scentbird.tictactoe.service.grpc.exceptions;

import com.scentbird.tictactoe.exception.GameNotFoundException;
import com.scentbird.tictactoe.exception.IllegalMoveException;
import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GRPCExceptionHandler {
    @GrpcExceptionHandler
    public Status handleGameNodFound(GameNotFoundException e) {
        return Status.NOT_FOUND.withDescription(e.getLocalizedMessage()).withCause(e);
    }

    @GrpcExceptionHandler
    public Status handleIllegalMove(IllegalMoveException e) {
        return Status.INVALID_ARGUMENT.withDescription(e.getLocalizedMessage()).withCause(e);
    }
}
