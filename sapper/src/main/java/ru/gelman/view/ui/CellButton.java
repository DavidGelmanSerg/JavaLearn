package ru.gelman.view.ui;

import ru.gelman.common.CellState;
import ru.gelman.common.dto.CellData;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CellButton extends JButton {
    private CellState previousState;
    private final int row;
    private final int col;

    public CellButton(int row, int col) {
        this.row = row;
        this.col = col;
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
                ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("/pictures/flag.png")));
                setIcon(img);
            }
            case SHOW_BOMB -> {
                if (previousState != CellState.MARKED) {
                    ImageIcon img = new ImageIcon(Objects.requireNonNull(getClass().getResource("/pictures/mine.png")));
                    setIcon(img);
                }
            }
        }
        previousState = state;
    }

    public boolean equalsByCoordinates(int x, int y) {
        return row == x && col == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellButton button = (CellButton) o;
        return row == button.row && col == button.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
