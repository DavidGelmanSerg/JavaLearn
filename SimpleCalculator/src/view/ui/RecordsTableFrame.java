package view.ui;

import common.dto.PlayerRecordData;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class RecordsTableFrame extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private final ResourceBundle difficultyLabels;
    private final ResourceBundle frameLabels;

    public RecordsTableFrame(List<PlayerRecordData> records, Locale locale) {
        difficultyLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_DIFFICULTIES, locale);
        frameLabels = ResourceBundle.getBundle(SapperViewPaths.LOCALIZATION_RECORDS, locale);

        JScrollPane recordsPanel = getRecordsPanel(records);
        this.setTitle(frameLabels.getString("title"));
        this.add(recordsPanel, BorderLayout.CENTER);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    private JScrollPane getRecordsPanel(List<PlayerRecordData> records) {
        JPanel panel = new JPanel(new GridLayout(records.size() + 1, 1));
        if (!records.isEmpty()) {
            panel.add(getHeader());
            records.forEach(recordData -> panel.add(getRecordRowPanel(recordData)));
        } else {
            panel.add(new JLabel(frameLabels.getString("not_found"), SwingConstants.CENTER));
            panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        }
        panel.revalidate();
        panel.repaint();

        JScrollPane pane = new JScrollPane(panel);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        return pane;
    }

    private JPanel getHeader() {
        JPanel headerPanel = new JPanel(new GridLayout(1, 3));
        headerPanel.add(getRecordRowTableItem(frameLabels.getString("player")));
        headerPanel.add(getRecordRowTableItem(frameLabels.getString("difficulty")));
        headerPanel.add(getRecordRowTableItem(frameLabels.getString("time")));

        headerPanel.revalidate();
        headerPanel.repaint();
        headerPanel.setPreferredSize(new Dimension(RecordsTableFrame.WIDTH, 30));
        return headerPanel;
    }

    private JLabel getRecordRowTableItem(String label) {
        JLabel item = new JLabel(label, SwingConstants.CENTER);
        item.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        item.setFont(new Font("Sans Serif", Font.PLAIN, 10));
        return item;
    }

    private JPanel getRecordRowPanel(PlayerRecordData record) {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        panel.add(getRecordRowTableItem(record.playerName()));
        panel.add(getRecordRowTableItem(difficultyLabels.getString(record.difficulty())));
        panel.add(getRecordRowTableItem(record.points()));
        panel.setPreferredSize(new Dimension(WIDTH, 30));
        return panel;
    }
}
