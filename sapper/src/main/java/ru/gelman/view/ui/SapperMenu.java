package ru.gelman.view.ui;

import ru.gelman.common.SapperDifficulty;
import ru.gelman.controller.SapperController;
import ru.gelman.view.bus.SapperViewEventBus;
import ru.gelman.view.bus.event.SapperViewEvent;
import ru.gelman.view.bus.event.SapperViewEventType;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Locale;
import java.util.ResourceBundle;

public class SapperMenu extends JMenuBar {
    private SapperController controller;
    private ResourceBundle menuLabels;
    private ResourceBundle difficultiesLabels;
    private Locale locale;

    public SapperMenu(Locale locale) {
        this.locale = locale;
        this.menuLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MENU, locale);
        this.difficultiesLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_DIFFICULTIES, locale);
        initHandlers();
        setMenu();
        this.setVisible(true);
    }

    public void setController(SapperController controller) {
        this.controller = controller;
    }

    private void initHandlers() {
        SapperViewEventBus.getInstance().register(SapperViewEventType.LOCALE_CHANGED, (SapperViewEvent<Locale> event) -> {
            changeLang(event.data());
            setMenu();
        });
    }

    private void changeLang(String lang) {
        this.locale = new Locale(lang);
        this.menuLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MENU, this.locale);
        this.difficultiesLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_DIFFICULTIES, this.locale);
    }

    private void changeLang(Locale locale) {
        this.locale = locale;
        this.menuLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MENU, this.locale);
        this.difficultiesLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_DIFFICULTIES, this.locale);
    }

    private void setMenu() {
        removeAll();
        JMenu gameMenu = new JMenu(menuLabels.getString("label"));
        JMenuItem newGame = new JMenuItem(menuLabels.getString("new_game"));
        newGame.addActionListener(action -> SapperViewEventBus.getInstance().publish(SapperViewEventType.START_GAME, new SapperViewEvent<>(SapperDifficulty.CURRENT)));

        JMenu difficultiesMenu = new JMenu(menuLabels.getString("difficulty"));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.BEGINNER, difficultiesLabels.getString("beginner")));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.AMATEUR, difficultiesLabels.getString("amateur")));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.PROFESSIONAL, difficultiesLabels.getString("professional")));

        JMenu langMenu = new JMenu(menuLabels.getString("lang.label"));
        JMenuItem ruLang = new JMenuItem(menuLabels.getString("lang.ru"));
        JMenuItem enLang = new JMenuItem(menuLabels.getString("lang.en"));
        ruLang.addActionListener(e -> {
            changeLang("ru");
            SapperViewEventBus.getInstance().publish(SapperViewEventType.LOCALE_CHANGED, new SapperViewEvent<>(new Locale("ru")));
        });
        enLang.addActionListener(e -> {
            changeLang("en");
            SapperViewEventBus.getInstance().publish(SapperViewEventType.LOCALE_CHANGED, new SapperViewEvent<>(new Locale("en")));
        });

        JMenu recordsTableMenu = new JMenu(menuLabels.getString("records"));
        recordsTableMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    controller.getPlayersRecords();
                }
            }
        });

        langMenu.add(enLang);
        langMenu.add(ruLang);

        gameMenu.add(newGame);
        gameMenu.add(difficultiesMenu);
        this.add(gameMenu);
        this.add(langMenu);
        this.add(recordsTableMenu);
        this.revalidate();
        this.repaint();
    }


    private static class difficultyMenuItem extends JMenuItem {
        public difficultyMenuItem(SapperDifficulty difficultyName, String itemText) {
            super(itemText);
            addActionListener(action -> SapperViewEventBus.getInstance().publish(SapperViewEventType.START_GAME, new SapperViewEvent<>(difficultyName)));
        }
    }
}
