package view.ui;

import common.SapperEventType;
import common.dto.CellData;
import controller.SapperController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SapperGameFrame extends JFrame implements PropertyChangeListener {
    private final SweeperMenu menu;
    private final StatePanel statePanel;
    private SapperController controller;
    private FieldPanel field;

    public SapperGameFrame(int w, int h) {
        setSize(w, h);
        centerWindow();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout(0, 5));
        menu = new SweeperMenu();
        setJMenuBar(menu);

        statePanel = new StatePanel();
        field = new FieldPanel();

        add(statePanel, BorderLayout.NORTH);
        add(field, BorderLayout.CENTER);

        setResizable(false);
        setVisible(true);
    }

    public void setController(SapperController controller) {
        this.controller = controller;
        field.setController(controller);
        menu.setController(controller);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        var eventType = (SapperEventType) evt.getOldValue();

        switch (eventType) {
            case START -> {
                int side = (int) evt.getNewValue();
                initField(side);
                statePanel.setStartSmile();
            }
            case CELLS_CHANGED -> {
                CellData[] cells = (CellData[]) evt.getNewValue();
                field.update(cells);
            }
            case FLAGS_CHANGED -> {
                int flags = (int) evt.getNewValue();
                statePanel.updateFlags(flags);
            }
            case LOOSE -> {
                CellData[] cellsWithBombs = (CellData[]) evt.getNewValue();
                field.update(cellsWithBombs);
                field.disableField();
                statePanel.setLooseSmile();
                JOptionPane.showMessageDialog(this, "You loose :(");
            }
            case WIN -> JOptionPane.showMessageDialog(this, "You win :)");
        }
    }

    private void centerWindow() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int xLocation = (screenSize.width - getWidth()) / 2;
        int yLocation = (screenSize.height - getHeight()) / 2;
        setLocation(xLocation, yLocation);
    }

    private void initField(int side) {
        remove(field);

        field = new FieldPanel(side);
        field.setController(controller);
        add(field);

        field.revalidate();
        field.repaint();
    }
}
