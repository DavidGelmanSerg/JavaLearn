package controller;

import common.SapperDifficulty;
import model.SapperModel;

public class SapperController {
    private SapperModel model;

    public SapperController(SapperModel model) {
        this.model = model;
    }

    public void setModel(SapperModel model) {
        this.model = model;
    }

    public void openCell(int x, int y) {
        model.openCell(x, y);
    }

    public void markCell(int x, int y) {
        model.markCell(x, y);
    }

    public void startNewGame(SapperDifficulty difficulty) {
        if (difficulty == SapperDifficulty.CURRENT) {
            model.restart();
        } else {
            model.start(difficulty);
        }
    }
}
