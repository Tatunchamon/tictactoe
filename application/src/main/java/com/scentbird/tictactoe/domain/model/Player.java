package com.scentbird.tictactoe.domain.model;

import java.util.Random;

public enum Player {
    X, O;

    private static final Player[] VALUES = values();
    private static final int SIZE = VALUES.length;
    private static final Random RANDOM = new Random();

    public static Player another(Player player) {
        return switch (player) {
            case X -> O;
            case O -> X;
        };
    }


    public static Player getRandomPlayer() {
        return VALUES[RANDOM.nextInt(SIZE)];
    }
}
