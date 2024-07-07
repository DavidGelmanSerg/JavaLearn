package controller;

import common.SapperDifficulty;

public class ClassicSapperController extends SapperController {
    public ClassicSapperController() {
    }

    @Override
    public void restartGame() {
        model.restart();
    }

    @Override
    public void openCell(int x, int y) {
        model.openCell(x, y);
    }

    @Override
    public void markCell(int x, int y) {
        model.markCell(x, y);
    }

    @Override
    public void startNewGame(SapperDifficulty difficulty) {
        model.start(difficulty);
    }
}
