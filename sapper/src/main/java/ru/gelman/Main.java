package ru.gelman;

import ru.gelman.controller.SapperController;
import ru.gelman.model.SapperModel;
import ru.gelman.timer.SapperTimer;
import ru.gelman.view.ui.SapperGameFrame;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        SapperModel model = new SapperModel();
        SapperGameFrame view = new SapperGameFrame(600, 600);
        SapperController controller = new SapperController();
        SapperTimer timer = new SapperTimer(TimeUnit.SECONDS, 1);

        model.addSapperListener(view);
        controller.setModel(model);
        view.setController(controller);
        timer.addTimerListener(model);
        model.addSapperListener(timer);

        model.start();
    }
}