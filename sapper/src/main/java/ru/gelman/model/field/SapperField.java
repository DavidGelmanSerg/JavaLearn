package ru.gelman.model.field;

import ru.gelman.common.CellState;
import ru.gelman.common.dto.CellData;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class SapperField {
    private final int side;
    private final List<FieldCell> cells;

    private final int bombs;
    private int flags;

    public SapperField(int side, int bombs) {
        this.side = side;
        this.bombs = bombs;
        this.flags = bombs;

        cells = new ArrayList<>(side);

        for (int i = 0; i < side; i++) {
            for (int j = 0; j < side; j++) {
                cells.add(new FieldCell(i, j));
            }
        }
    }

    public List<CellData> open(int x, int y) {
        checkCoordinates(x, y);
        var cell = getCell(x, y);

        if (cell.isOpened() || cell.isMarked()) {
            return null;
        }

        List<CellData> openedCells = new ArrayList<>();
        Queue<FieldCell> toOpen = new LinkedBlockingQueue<>();

        toOpen.add(cell);

        while (!toOpen.isEmpty()) {
            var currentCell = toOpen.poll();

            currentCell.setState(CellState.OPENED);
            openedCells.add(currentCell.getCellData());

            if (!currentCell.hasBombsAround() && !currentCell.hasBomb()) {
                toOpen.addAll(getClosedCellsAround(currentCell));
            }
        }
        return openedCells;
    }

    public void mark(int x, int y) {
        checkCoordinates(x, y);
        var cell = getCell(x, y);

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
        FieldCell cell = getCell(x, y);
        return cell.getCellData();
    }

    public List<CellData> getCellsWhenLoose(int x, int y) {
        checkCoordinates(x, y);
        List<CellData> cellsWithBombs = new ArrayList<>();

        FieldCell looseCell = getCell(x, y);
        looseCell.setState(CellState.SHOW_BOMB);
        cellsWithBombs.add(looseCell.getCellData());

        for (FieldCell cell : cells) {
            if (cell.hasBomb()) {
                cell.setState(CellState.SHOW_BOMB);
                cellsWithBombs.add(cell.getCellData());
            }
        }

        return cellsWithBombs;
    }

    public int getSide() {
        return side;
    }

    public void setBombs(int x, int y) {
        List<FieldCell> cellsRegister = new ArrayList<>(List.copyOf(cells));
        cellsRegister.remove(getCell(x, y));

        Random random = new Random();
        for (int i = 0; i < bombs; i++) {
            FieldCell cellFromRegister = cellsRegister.get(random.nextInt(cellsRegister.size()));
            FieldCell cellToSetBomb = getCell(cellFromRegister.getRow(), cellFromRegister.getColumn());

            cellToSetBomb.setBomb();
            cellsRegister.remove(cellFromRegister);

            List<FieldCell> cellsAround = getCellsAround(cellToSetBomb);
            for (FieldCell c : cellsAround) {
                c.incrementBombsAround();
            }
        }
    }

    private FieldCell getCell(int x, int y) {
        for (FieldCell cell : cells) {
            if (cell.equalsCoordinates(x, y)) {
                return cell;
            }
        }
        throw new NullPointerException("No cell was found with coordinates: [" + x + ", " + y + "]");
    }

    public boolean isWin() {
        int openedCellsAmount = 0;
        for (FieldCell cell : cells) {
            if (cell.isOpened()) {
                openedCellsAmount++;
            }
        }

        int cellsAmountExceptBombs = (side * side) - bombs;
        return (openedCellsAmount == cellsAmountExceptBombs);
    }

    public boolean isBomb(int x, int y) {
        checkCoordinates(x, y);
        FieldCell cell = getCell(x, y);
        return cell.hasBomb();
    }

    public boolean isMarked(int x, int y) {
        checkCoordinates(x, y);
        FieldCell cell = getCell(x, y);
        return cell.isMarked();
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
                    FieldCell currentCell = getCell(horizontalOffset, verticalOffset);

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
                    FieldCell currentCell = getCell(horizontalOffset, verticalOffset);

                    if (!currentCell.equals(cell) && currentCell.isClosed()) {
                        cellsAround.add(currentCell);
                    }
                }
            }
        }

        return cellsAround;
    }

    private boolean isInsideField(int x, int y) {
        return x >= 0 && x < side && y >= 0 && y < side;
    }

    private void checkCoordinates(int x, int y) {
        if (!isInsideField(x, y)) {
            throw new IndexOutOfBoundsException("Incorrect coordinates [" + x + "," + y + "] in field of size" + side + "x" + side);
        }
    }
}
