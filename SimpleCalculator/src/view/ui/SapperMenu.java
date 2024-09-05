package view.ui;

import common.SapperDifficulty;
import view.bus.SapperViewEventBus;
import view.bus.event.LocaleChangedEvent;
import view.bus.event.SapperViewEventType;
import view.bus.event.StartGameEvent;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class SapperMenu extends JMenuBar {
    private ResourceBundle menuLabels;
    private Locale locale;

    public SapperMenu(Locale locale) {
        this.locale = locale;
        this.menuLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MENU, locale);
        initHandlers();
        setMenuLabels();
        this.setVisible(true);
    }

    private void initHandlers() {
        SapperViewEventBus.getInstance().register(SapperViewEventType.LOCALE_CHANGED, (LocaleChangedEvent event) -> {
            changeLang(event.getData());
            setMenuLabels();
        });
    }

    private void changeLang(String lang) {
        this.locale = new Locale(lang);
        this.menuLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MENU, this.locale);
    }

    private void changeLang(Locale locale) {
        this.locale = locale;
        this.menuLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MENU, this.locale);
    }

    private void setMenuLabels() {
        removeAll();
        JMenu gameMenu = new JMenu(menuLabels.getString("label"));
        JMenuItem newGame = new JMenuItem(menuLabels.getString("new_game"));
        newGame.addActionListener(action -> SapperViewEventBus.getInstance().publish(SapperViewEventType.START_GAME, new StartGameEvent(SapperDifficulty.CURRENT)));

        JMenu difficultiesMenu = new JMenu(menuLabels.getString("difficulty"));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.BEGINNER, menuLabels.getString("beginner")));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.AMATEUR, menuLabels.getString("amateur")));
        difficultiesMenu.add(new difficultyMenuItem(SapperDifficulty.PROFESSIONAL, menuLabels.getString("professional")));

        JMenu langMenu = new JMenu(menuLabels.getString("lang.label"));
        JMenuItem ruLang = new JMenuItem(menuLabels.getString("lang.ru"));
        JMenuItem enLang = new JMenuItem(menuLabels.getString("lang.en"));
        ruLang.addActionListener(e -> {
            changeLang("ru");
            SapperViewEventBus.getInstance().publish(SapperViewEventType.LOCALE_CHANGED, new LocaleChangedEvent(new Locale("ru")));
        });
        enLang.addActionListener(e -> {
            changeLang("en");
            SapperViewEventBus.getInstance().publish(SapperViewEventType.LOCALE_CHANGED, new LocaleChangedEvent(new Locale("en")));
        });

        langMenu.add(enLang);
        langMenu.add(ruLang);

        gameMenu.add(newGame);
        gameMenu.add(difficultiesMenu);
        this.add(gameMenu);
        this.add(langMenu);
        this.revalidate();
        this.repaint();
    }


    private static class difficultyMenuItem extends JMenuItem {
        public difficultyMenuItem(SapperDifficulty difficultyName, String itemText) {
            super(itemText);
            addActionListener(action -> SapperViewEventBus.getInstance().publish(SapperViewEventType.START_GAME, new StartGameEvent(difficultyName)));
        }
    }
}
