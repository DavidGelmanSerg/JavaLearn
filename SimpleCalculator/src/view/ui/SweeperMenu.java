package view.ui;

import common.SapperDifficulty;
import controller.SapperController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SweeperMenu extends JMenuBar {
    private SapperController controller;

    public SweeperMenu() {
        JMenu menu = new JMenu("Меню");

        JMenuItem newGame = new JMenuItem("Новая игра");
        newGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restartGame();
            }
        });

        JMenu difficultiesMenu = new JMenu("Выбрать сложность");
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.BEGINNER_DIFFICULTY, "Новичок"));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.AMATEUR_DIFFICULTY, "Любитель"));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.PROFESSIONAL_DIFFICULTY, "Профессионал"));

        menu.add(newGame);
        menu.add(difficultiesMenu);
        add(menu);
        setVisible(true);
    }

    public void setController(SapperController controller) {
        this.controller = controller;
    }

    private class difficultyMenuItem extends JMenuItem {
        public difficultyMenuItem(SapperDifficulty difficultyName, String itemText) {
            super(itemText);
            addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    controller.startNewGame(difficultyName);
                }
            });
        }
    }
}
