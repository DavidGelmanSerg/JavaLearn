package controller;

import common.SapperDifficulty;
import model.SapperModel;

public abstract class SapperController {
    protected SapperModel model;

    public SapperController() {
    }

    public void setModel(SapperModel model) {
        this.model = model;
    }

    public abstract void startNewGame(SapperDifficulty difficulty);

    public abstract void restartGame();

    public abstract void openCell(int x, int y);

    public abstract void markCell(int x, int y);
}
