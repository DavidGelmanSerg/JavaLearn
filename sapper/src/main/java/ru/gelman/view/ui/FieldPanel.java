package ru.gelman.view.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gelman.common.dto.CellData;
import ru.gelman.controller.SapperController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FieldPanel extends JPanel {
    private final List<CellButton> buttons;
    private SapperController controller;

    private final Logger logger = LoggerFactory.getLogger(SapperGameFrame.class);
    public FieldPanel(int side) {
        buttons = new ArrayList<>(side);
        GridLayout layout = new GridLayout(side, side, 0, 0);
        setLayout(layout);

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                CellButton button = createCellButton(i, j);
                buttons.add(button);
                add(button);
            }
        }
        setVisible(true);
    }

    public void setController(SapperController controller) {
        this.controller = controller;
    }

    public void update(List<CellData> cellData) {
        int counter = 0;
        logger.info("Updating {} cell buttons", cellData.size());
        for (CellData cell : cellData) {
            int x = cell.getRow();
            int y = cell.getColumn();
            CellButton button = Objects.requireNonNull(getButtonByCoordinates(x, y));
            button.update(cell);
            counter++;
        }
        logger.info("{} cells are being updated successfully", counter);
    }

    public void disableField() {
        for (Component component : getComponents()) {
            for (MouseListener listener : component.getMouseListeners()) {
                component.removeMouseListener(listener);
            }
        }
    }

    private CellButton createCellButton(int row, int col) {
        CellButton button = new CellButton(row, col);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int clickedBtn = e.getButton();
                if (clickedBtn == MouseEvent.BUTTON1) {
                    logger.info("Left click of the {}", button);
                    controller.openCell(row, col);
                } else if (clickedBtn == MouseEvent.BUTTON3) {
                    logger.info("Right click of the {}", button);
                    controller.markCell(row, col);
                }
            }
        });
        return button;
    }

    private CellButton getButtonByCoordinates(int x, int y) {
        for (CellButton button : buttons) {
            if (button.equalsByCoordinates(x, y)) {
                return button;
            }
        }
        return null;
    }
}
