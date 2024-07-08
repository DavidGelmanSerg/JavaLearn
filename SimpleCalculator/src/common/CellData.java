package common;

public class CellData {
    private final CellState state;
    private final int row;
    private final int column;
    private final int bombsAround;

    public CellData(int row, int col, int bombsAround, CellState state) {
        this.row = row;
        column = col;
        this.bombsAround = bombsAround;
        this.state = state;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public CellState getState() {
        return state;
    }
}
