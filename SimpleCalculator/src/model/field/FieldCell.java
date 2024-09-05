package model.field;

import common.CellState;
import common.dto.CellData;

public class FieldCell {
    private final int row;
    private final int column;
    private boolean hasBomb;
    private CellState state;
    private int bombsAround;

    public FieldCell(int row, int column) {
        this.row = row;
        this.column = column;
        bombsAround = 0;
        hasBomb = false;
        this.state = CellState.CLOSED;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean hasBomb() {
        return hasBomb;
    }

    public void setBomb() {
        hasBomb = true;
    }


    public boolean hasBombsAround() {
        return bombsAround > 0;
    }

    public void incrementBombsAround() {
        bombsAround++;
    }

    public CellData getCellData() {
        return new CellData(row, column, bombsAround, state);
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isClosed() {
        return state == CellState.CLOSED;
    }

    public boolean isOpened() {
        return state == CellState.OPENED;
    }

    public boolean isMarked() {
        return state == CellState.MARKED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldCell cell = (FieldCell) o;
        return row == cell.row && column == cell.column;
    }

    public boolean equalsCoordinates(int x, int y) {
        return row == x && column == y;
    }

    @Override
    public String toString() {
        return "[" + row + "," + column + "]";
    }
}
