package ru.gelman.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gelman.common.SapperDifficulty;
import ru.gelman.model.SapperModel;

public class SapperController {
    private SapperModel model;
    private final Logger logger = LoggerFactory.getLogger(SapperController.class);

    public void setModel(SapperModel model) {
        this.model = model;
    }

    public void openCell(int x, int y) {
        logger.info("Sending OPEN cell ({},{}) request to model",x,y);
        model.openCell(x, y);
    }

    public void markCell(int x, int y) {
        logger.info("Sending MARK cell ({},{}) request to model",x,y);
        model.markCell(x, y);
    }

    public void startNewGame(SapperDifficulty difficulty) {
        logger.info("Sending START NEW GAME request to model. Difficulty: {}",difficulty);
        if (difficulty == SapperDifficulty.CURRENT) {
            model.restart();
        } else {
            model.start(difficulty);
        }
    }

    public void getPlayersRecords() {
        model.getPlayersRecords();
    }

    public void saveRecord(String playerName) {
        model.saveRecord(playerName);
    }
}
