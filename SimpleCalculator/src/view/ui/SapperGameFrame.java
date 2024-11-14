package view.ui;

import common.SapperDifficulty;
import common.dto.PlayerRecordData;
import common.observer.SapperListener;
import common.observer.events.SapperEvent;
import common.observer.events.SapperEventType;
import controller.SapperController;
import view.bus.SapperViewEventBus;
import view.bus.event.SapperViewEvent;
import view.bus.event.SapperViewEventType;

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

    public SapperGameFrame(int w, int h) {
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
                    throw new RuntimeException("Unable to save app configuration to file: " + SapperViewPaths.CONFIG);
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
        revalidate();
        repaint();
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
                    initField(event.getSide());
                    statePanel.updateFlags(event.getFlags());
                    statePanel.setStartSmile();
                    statePanel.updateTime("00:00");
                }
                case LOOSE -> {
                    field.disableField();
                    statePanel.setLooseSmile();
                    JOptionPane.showMessageDialog(this, messages.getString("loose"));
                }
                case WIN -> {
                    field.disableField();
                    if (event.isRecord()) {
                        String playerName = JOptionPane.showInputDialog(messages.getString("win.record"));
                        controller.saveRecord(playerName);
                    } else {
                        JOptionPane.showMessageDialog(this, messages.getString("win"));
                    }
                }
                case SAVE_RECORD -> {
                    String message;
                    if (event.isRecordSaved()) {
                        message = messages.getString("win.record.saved");
                    } else {
                        message = messages.getString("win.record.not_saved");
                    }
                    JOptionPane.showMessageDialog(this, message);
                }
                case CELLS_CHANGED -> field.update(event.getCellData());
                case FLAGS_CHANGED -> statePanel.updateFlags(event.getFlags());
                case TIME_CHANGED -> statePanel.updateTime(event.getTime().toString());
                case RECORDS_SELECTED -> showRecords(event.getRecords());
            }
        });
    }
}