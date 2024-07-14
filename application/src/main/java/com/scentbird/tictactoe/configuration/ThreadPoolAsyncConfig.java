package com.scentbird.tictactoe.configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPoolAsyncConfig {

    @Value("${application.threadpool.size}")
    private int threadPoolSize;

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolSize);
    }
}
