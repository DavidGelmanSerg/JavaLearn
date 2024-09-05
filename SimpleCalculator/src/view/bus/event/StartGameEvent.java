package view.bus.event;

import common.SapperDifficulty;

public class StartGameEvent implements SapperViewEvent<SapperDifficulty> {
    private final SapperDifficulty difficulty;

    public StartGameEvent(SapperDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public SapperDifficulty getData() {
        return difficulty;
    }
}
