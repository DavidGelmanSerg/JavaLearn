package ru.gelman.model.field;

import ru.gelman.common.SapperDifficulty;

public class SapperFieldFactory {
    private static int side;
    private static int bombs;

    public static SapperField getField(SapperDifficulty difficulty) {
        switch (difficulty) {
            case BEGINNER -> {
                side = 8;
                bombs = 10;
                return new SapperField(8, 10);
            }
            case AMATEUR -> {
                side = 16;
                bombs = 40;
                return new SapperField(16, 40);
            }
            case PROFESSIONAL -> {
                side = 32;
                bombs = 99;
                return new SapperField(32, 99);
            }
            case CURRENT -> {
                return new SapperField(side, bombs);
            }
            default -> throw new IllegalArgumentException("Unsupported difficulty while creating the field!");
        }
    }
}
