package model.field;

import common.CellState;

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

    public int getBombsAround() {
        return bombsAround;
    }

    public boolean hasBombsAround() {
        return bombsAround > 0;
    }

    public void incrementBombsAround() {
        bombsAround++;
    }

    public CellState getState() {
        return state;
    }

    public void setState(CellState state) {
        this.state = state;
    }

    public boolean isClosed() {
        return state.equals(CellState.CLOSED);
    }

    public boolean isOpened() {
        return state.equals(CellState.OPENED);
    }

    public boolean isMarked() {
        return state.equals(CellState.MARKED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldCell cell = (FieldCell) o;
        return row == cell.row && column == cell.column;
    }

    @Override
    public String toString() {
        return "[" + row + "," + column + "]";
    }
}
