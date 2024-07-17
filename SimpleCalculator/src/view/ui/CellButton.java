package view.ui;

import common.CellState;
import common.dto.CellData;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CellButton extends JButton {
    private CellState previousState;

    public CellButton() {
        previousState = CellState.CLOSED;
        setBackground(Color.WHITE);
        setFont(new Font("Sans Serif", Font.BOLD, 24));
    }

    public void update(CellData cell) {
        CellState state = cell.getState();
        switch (state) {
            case CLOSED -> setIcon(null);
            case OPENED -> {
                int bombsAround = cell.getBombsAround();

                if (bombsAround > 0) {
                    setText(String.valueOf(bombsAround));
                }

                setBackground(Color.LIGHT_GRAY);
            }
            case MARKED -> {
                ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("pictures/flag.png")));
                setIcon(img);
            }
            case SHOW_BOMB -> {
                if (!previousState.equals(CellState.MARKED)) {
                    ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("pictures/mine.png")));
                    setIcon(img);
                }
            }
        }
        previousState = state;
    }
}
