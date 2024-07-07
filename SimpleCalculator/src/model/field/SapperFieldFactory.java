package model.field;

import common.SapperDifficulty;

public class SapperFieldFactory {
    public static SapperField getField(SapperDifficulty difficulty) {
        return switch (difficulty) {
            case BEGINNER_DIFFICULTY -> new SapperField(8, 10);
            case AMATEUR_DIFFICULTY -> new SapperField(16, 40);
            case PROFESSIONAL_DIFFICULTY -> new SapperField(32, 99);
        };
    }
}
