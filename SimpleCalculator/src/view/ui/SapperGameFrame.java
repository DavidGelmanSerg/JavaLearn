package view.ui;

import common.observer.SapperListener;
import common.observer.events.SapperEvent;
import common.observer.events.SapperEventType;
import controller.SapperController;
import view.bus.SapperViewEventBus;
import view.bus.event.LocaleChangedEvent;
import view.bus.event.SapperViewEventType;
import view.bus.event.StartGameEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class SapperGameFrame extends JFrame implements SapperListener {
    private final StatePanel statePanel;
    private final Properties config = new Properties();
    private ResourceBundle messages;
    private Locale currentLocale;
    private SapperController controller;
    private FieldPanel field;

    public SapperGameFrame(int w, int h) {
        this.setSize(w, h);
        this.centerWindow();
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.initLocale();

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try (FileOutputStream fos = new FileOutputStream(SapperViewPaths.CONFIG)) {
                    config.put("lang", currentLocale.getLanguage());
                    config.store(fos, "");
                } catch (IOException ex) {
                    throw new RuntimeException("Unable to save app configuration to file: " + SapperViewPaths.CONFIG);
                }
            }
        });

        this.initEventHandlers();

        this.setLayout(new BorderLayout(0, 5));
        this.setJMenuBar(new SapperMenu(this.currentLocale));

        statePanel = new StatePanel();
        this.add(statePanel, BorderLayout.NORTH);
        this.setVisible(true);
    }

    public void setController(SapperController controller) {
        this.controller = controller;
    }

    private void centerWindow() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int xLocation = (screenSize.width - getWidth()) / 2;
        int yLocation = (screenSize.height - getHeight()) / 2;
        setLocation(xLocation, yLocation);
    }

    private void initLocale() {
        try (FileInputStream inputStream = new FileInputStream(SapperViewPaths.CONFIG)) {
            this.config.load(inputStream);
            this.currentLocale = new Locale(this.config.getProperty("lang"));
        } catch (IOException e) {
            this.currentLocale = Locale.getDefault();
            this.config.put("lang", currentLocale.getLanguage());
        }
        this.messages = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MESSAGES, this.currentLocale);
    }

    private void initField(int side) {
        if (field != null) {
            remove(field);
        }

        field = new FieldPanel(side);
        field.setController(controller);
        add(field, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }

    private void initEventHandlers() {
        SapperViewEventBus viewBus = SapperViewEventBus.getInstance();
        viewBus.register(SapperViewEventType.LOCALE_CHANGED, (LocaleChangedEvent event) -> changeLocale(event.getData()));
        viewBus.register(SapperViewEventType.START_GAME, (StartGameEvent event) -> controller.startNewGame(event.getData()));
    }

    private void changeLocale(Locale locale) {
        this.currentLocale = locale;
        this.messages = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MESSAGES, this.currentLocale);
    }

    @Override
    public void update(SapperEvent event) {
        SapperEventType eventType = event.type();
        switch (eventType) {
            case START -> {
                initField(event.side());
                statePanel.setStartSmile();
            }
            case LOOSE -> {
                field.update(event.cellData());
                field.disableField();
                statePanel.setLooseSmile();
                JOptionPane.showMessageDialog(this, messages.getString("loose"));
            }
            case WIN -> JOptionPane.showMessageDialog(this, messages.getString("win"));
            case CELLS_CHANGED -> field.update(event.cellData());
            case FLAGS_CHANGED -> statePanel.updateFlags(event.flags());
        }
    }
}