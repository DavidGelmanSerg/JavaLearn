package model.field;

import common.CellData;
import common.CellState;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class SapperField {
    private final int side;
    private final FieldCell[][] cells;
    private final int bombs;
    private int flags;

    public SapperField(int side, int bombs) {
        this.side = side;
        this.bombs = bombs;
        this.flags = bombs;

        cells = new FieldCell[side][side];

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                cells[i][j] = new FieldCell(i, j);
            }
        }
    }

    public CellData[] open(int x, int y) {
        checkCoordinates(x, y);
        var cell = cells[x][y];

        if (cell.isOpened() || cell.isMarked()) {
            return new CellData[0];
        }

        List<CellData> openedCells = new ArrayList<>();
        Queue<FieldCell> toOpen = new LinkedBlockingQueue<>();

        toOpen.add(cell);

        while (!toOpen.isEmpty()) {
            var currentCell = toOpen.poll();

            currentCell.setState(CellState.OPENED);
            openedCells.add(getCellData(currentCell));

            if (!currentCell.hasBombsAround() && !currentCell.hasBomb()) {
                toOpen.addAll(getClosedCellsAround(currentCell));
            }
        }
        return openedCells.toArray(new CellData[0]);
    }

    public void mark(int x, int y) {
        checkCoordinates(x, y);
        var cell = cells[x][y];

        if (cell.isClosed()) {
            cell.setState(CellState.MARKED);
            flags--;
        } else if (cell.isMarked()) {
            cell.setState(CellState.CLOSED);
            flags++;
        }
    }

    public int getFlags() {
        return flags;
    }

    public CellData getCellData(int x, int y) {
        checkCoordinates(x, y);
        return getCellData(cells[x][y]);
    }

    public CellData[] getCellsWhenLoose(int x, int y) {
        checkCoordinates(x, y);
        List<CellData> cellsWithBombs = new ArrayList<>();

        var looseCell = cells[x][y];
        looseCell.setState(CellState.SHOW_BOMB);
        cellsWithBombs.add(getCellData(looseCell));

        for (FieldCell[] cellRow : cells) {
            for (FieldCell cell : cellRow) {
                if (cell.hasBomb()) {
                    cell.setState(CellState.SHOW_BOMB);
                    cellsWithBombs.add(getCellData(cell));
                }
            }
        }

        return cellsWithBombs.toArray(new CellData[0]);
    }

    public int getSide() {
        return side;
    }

    public void setBombs(int x, int y) {
        checkCoordinates(x, y);
        Random random = new Random();

        var cell = cells[x][y];
        int bombsCount = 0;
        List<FieldCell> cellsWithBombs = new ArrayList<>();
        while (bombsCount != bombs) {
            int row = random.nextInt(0, side);
            int column = random.nextInt(0, side);

            FieldCell randomCell = cells[row][column];

            if (!randomCell.equals(cell) && !cellsWithBombs.contains(randomCell)) {
                randomCell.setBomb();
                cellsWithBombs.add(randomCell);

                List<FieldCell> cellsAround = getCellsAround(randomCell);
                for (FieldCell c : cellsAround) {
                    c.incrementBombsAround();
                }
                bombsCount++;
            }
        }
    }

    public boolean isWin() {
        int openedCellsAmount = 0;
        for (FieldCell[] cellRow : cells) {
            for (FieldCell cell : cellRow) {
                if (cell.isOpened()) {
                    openedCellsAmount++;
                }
            }
        }
        int cellsAmountExceptBombs = (side * side) - bombs;
        return (openedCellsAmount == cellsAmountExceptBombs);
    }

    public boolean isBomb(int x, int y) {
        checkCoordinates(x, y);
        return cells[x][y].hasBomb();
    }

    public boolean isMarked(int x, int y) {
        checkCoordinates(x, y);
        return cells[x][y].isMarked();
    }

    private List<FieldCell> getCellsAround(FieldCell cell) {
        List<FieldCell> cellsAround = new ArrayList<>();

        int lBound = -1;
        int rBound = 1;
        int bBound = -1;
        int tBound = 1;

        for (int i = lBound; i <= rBound; i++) {
            for (int j = tBound; j >= bBound; j--) {
                int horizontalOffset = cell.getRow() + i;
                int verticalOffset = cell.getColumn() - j;

                if (isInsideField(horizontalOffset, verticalOffset)) {
                    FieldCell currentCell = cells[horizontalOffset][verticalOffset];

                    if (!currentCell.equals(cell)) {
                        cellsAround.add(currentCell);
                    }
                }
            }
        }

        return cellsAround;
    }

    private List<FieldCell> getClosedCellsAround(FieldCell cell) {
        List<FieldCell> cellsAround = new ArrayList<>();

        int lBound = -1;
        int rBound = 1;
        int bBound = -1;
        int tBound = 1;

        for (int i = lBound; i <= rBound; i++) {
            for (int j = tBound; j >= bBound; j--) {
                int horizontalOffset = cell.getRow() + i;
                int verticalOffset = cell.getColumn() - j;

                if (isInsideField(horizontalOffset, verticalOffset)) {
                    FieldCell currentCell = cells[horizontalOffset][verticalOffset];

                    if (!currentCell.equals(cell) && currentCell.isClosed()) {
                        cellsAround.add(currentCell);
                    }
                }
            }
        }

        return cellsAround;
    }

    private CellData getCellData(FieldCell cell) {
        int row = cell.getRow();
        int col = cell.getColumn();
        int bombsAround = cell.getBombsAround();
        CellState state = cell.getState();

        return new CellData(row, col, bombsAround, state);
    }

    private boolean isInsideField(int x, int y) {
        return x >= 0 && x < side && y >= 0 && y < side;
    }

    public void checkCoordinates(int x, int y) {
        if (!isInsideField(x, y)) {
            var cell = cells[x][y];
            throw new IndexOutOfBoundsException("Incorrect coordinates of cell " + cell + " in field of size" + side + "x" + side);
        }
    }
}
