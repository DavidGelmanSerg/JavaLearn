package view.ui.field;

import common.CellData;
import controller.SapperController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class FieldPanel extends JPanel {
    private SapperController controller;
    private CellButton[][] cells;

    public FieldPanel(int side) {
        cells = new CellButton[side][side];
        GridLayout layout = new GridLayout(side, side, 0, 0);
        setLayout(layout);

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                CellButton button = new CellButton();
                int row = i;
                int col = j;
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int clickedBtn = e.getButton();
                        if (clickedBtn == MouseEvent.BUTTON1) {
                            controller.openCell(row, col);
                        } else if (clickedBtn == MouseEvent.BUTTON3) {
                            controller.markCell(row, col);
                        }
                    }
                });

                cells[row][col] = button;
                add(button);
            }
        }
        setVisible(true);
    }

    public FieldPanel() {
    }

    public void setController(SapperController controller) {
        this.controller = controller;
    }

    public void update(CellData[] cellData) {
        for (CellData cell : cellData) {
            int x = cell.getRow();
            int y = cell.getColumn();
            cells[x][y].update(cell);
        }
    }

    public void disableField() {
        for (Component component : getComponents()) {
            for (MouseListener listener : component.getMouseListeners()) {
                component.removeMouseListener(listener);
            }
        }
    }
}
