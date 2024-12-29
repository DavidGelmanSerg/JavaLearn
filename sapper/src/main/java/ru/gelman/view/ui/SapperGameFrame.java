package ru.gelman.view.ui;

import org.slf4j.Logger;
import ru.gelman.common.SapperDifficulty;
import ru.gelman.common.dto.PlayerRecordData;
import ru.gelman.common.observer.SapperListener;
import ru.gelman.common.observer.events.SapperEvent;
import ru.gelman.common.observer.events.SapperEventType;
import ru.gelman.controller.SapperController;
import ru.gelman.view.bus.SapperViewEventBus;
import ru.gelman.view.bus.event.SapperViewEvent;
import ru.gelman.view.bus.event.SapperViewEventType;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class SapperGameFrame extends JFrame implements SapperListener {
    private final StatePanel statePanel;
    private final Properties config = new Properties();
    private final SapperMenu menu;
    private ResourceBundle messages;
    private Locale currentLocale;
    private SapperController controller;
    private FieldPanel field;
    private final Logger logger = LoggerFactory.getLogger(SapperGameFrame.class);
    public SapperGameFrame(int w, int h) {
        logger.info("initializing view...");
        this.setSize(w, h);
        this.centerWindow();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(0, 5));
        this.initLocale();
        this.initEventHandlers();

        this.menu = new SapperMenu(currentLocale);
        this.setJMenuBar(menu);

        statePanel = new StatePanel();
        this.add(statePanel, BorderLayout.NORTH);
        this.setVisible(true);
        logger.info("Completed successful");
    }

    private void initEventHandlers() {
        SapperViewEventBus viewBus = SapperViewEventBus.getInstance();
        viewBus.register(SapperViewEventType.LOCALE_CHANGED, (SapperViewEvent<Locale> event) -> changeLocale(event.data()));
        viewBus.register(SapperViewEventType.START_GAME, (SapperViewEvent<SapperDifficulty> event) -> controller.startNewGame(event.data()));
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try (FileOutputStream fos = new FileOutputStream(SapperViewPaths.CONFIG)) {
                    config.put("lang", currentLocale.getLanguage());
                    config.store(fos, "");
                } catch (IOException ex) {
                    throw new RuntimeException("unable to save app configuration to file: " + SapperViewPaths.CONFIG);
                }
            }
        });
    }

    public void setController(SapperController controller) {
        this.controller = controller;
        this.menu.setController(controller);
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
            logger.info("Found locale {} in config {}", this.currentLocale.getLanguage(),SapperViewPaths.CONFIG);
        } catch (IOException e) {
            this.currentLocale = Locale.getDefault();
            this.config.put("lang", this.currentLocale.getLanguage());
            logger.info("Config file not found. Setting locale to default: {}", this.currentLocale.getLanguage());
        }

        this.messages = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MESSAGES, this.currentLocale);
    }

    private void initField(int side) {
        logger.info("Initializing the field with size {}x{}",side,side);
        if (field != null) {
            remove(field);
        }

        field = new FieldPanel(side);
        field.setController(controller);

        add(field, BorderLayout.CENTER);
        revalidate();
        repaint();
        logger.info("Completed successful");
    }

    private void changeLocale(Locale locale) {
        this.currentLocale = locale;
        this.messages = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_MESSAGES, currentLocale);
    }

    private void showRecords(List<PlayerRecordData> records) {
        this.setEnabled(false);
        RecordsTableFrame recordsFrame = new RecordsTableFrame(records, currentLocale);
        recordsFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setEnabled(true);
            }
        });
    }

    @Override
    public void update(SapperEvent event) {
        EventQueue.invokeLater(() -> {
            SapperEventType eventType = event.getType();
            switch (eventType) {
                case INIT -> {
                    logger.info("Got INIT event from model");
                    initField(event.getSide());
                    statePanel.updateFlags(event.getFlags());
                    statePanel.setStartSmile();
                    statePanel.updateTime("00:00");
                }
                case LOOSE -> {
                    logger.info("Got LOOSE event from model");
                    field.disableField();
                    statePanel.setLooseSmile();
                    JOptionPane.showMessageDialog(this, messages.getString("loose"));
                }
                case WIN -> {
                    logger.info("Got WIN event from model");
                    field.disableField();
                    if (event.isRecord()) {
                        logger.info("is record. Requesting player's name");
                        String playerName = JOptionPane.showInputDialog(messages.getString("win.record"));
                        logger.info("Saving record for player: {}",playerName);
                        controller.saveRecord(playerName);
                    } else {
                        logger.info("not record");
                        JOptionPane.showMessageDialog(this, messages.getString("win"));
                    }
                }
                case SAVE_RECORD -> {
                    logger.info("Got SAVE_RECORD event from model");
                    String message;
                    if (event.isRecordSaved()) {
                        logger.info("Record successfully saved");
                        message = messages.getString("win.record.saved");
                    } else {
                        logger.info("Record saving failed");
                        message = messages.getString("win.record.not_saved");
                    }
                    JOptionPane.showMessageDialog(this, message);
                }
                case CELLS_CHANGED -> {
                    logger.info("Got CELLS_CHANGED event from model");
                    field.update(event.getCellData());
                }
                case FLAGS_CHANGED -> {
                    logger.info("Got FLAGS_CHANGED event from model");
                    statePanel.updateFlags(event.getFlags());

                }
                case TIME_CHANGED -> statePanel.updateTime(event.getTime().toString());
                case RECORDS_SELECTED -> {
                    logger.info("Got RECORDS_SELECTED event from model");
                    showRecords(event.getRecords());
                }
            }
        });
    }

    @Override
    public String toString() {
        return "Desktop UI: Sapper Game Frame (Swing)";
    }
}