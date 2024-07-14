package com.scentbird.tictactoe.configuration;

import com.scentbird.tictactoe.TicTacToeServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GRPCClientConfig {
    @GrpcClient("grpc-server")
    private TicTacToeServiceGrpc.TicTacToeServiceBlockingStub ticTacToeStub;

    @GrpcClient("grpc-server")
    private TicTacToeServiceGrpc.TicTacToeServiceStub ticTacToeStubAsync;

    @Bean
    public TicTacToeServiceGrpc.TicTacToeServiceBlockingStub ticTacToeStub() {
        return ticTacToeStub;
    }

    @Bean
    public TicTacToeServiceGrpc.TicTacToeServiceStub ticTacToeStubAsync() {
        return ticTacToeStubAsync;
    }
}
