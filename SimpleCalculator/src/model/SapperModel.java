package model;

import common.CellData;
import common.SapperDifficulty;
import common.SapperEventType;
import model.field.SapperField;
import model.field.SapperFieldFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SapperModel {
    private final PropertyChangeSupport support;
    private SapperDifficulty difficulty;
    private SapperGameStatus status;
    private SapperField field;

    public SapperModel() {
        support = new PropertyChangeSupport(this);
        difficulty = SapperDifficulty.BEGINNER_DIFFICULTY;
        setStatus(SapperGameStatus.NOT_STARTED);
        field = SapperFieldFactory.getField(difficulty);
    }

    public void start() {
        setStatus(SapperGameStatus.STARTED);
        firePropertyChange(SapperEventType.START, field.getSide());
        firePropertyChange(SapperEventType.FLAGS_CHANGED, field.getFlags());
    }

    public void start(SapperDifficulty difficulty) {
        this.difficulty = difficulty;
        field = SapperFieldFactory.getField(difficulty);
        start();
    }

    public void restart() {
        field = SapperFieldFactory.getField(difficulty);
        start();
    }

    public void openCell(int x, int y) {
        if (isStarted()) {
            field.setBombs(x, y);
            setStatus(SapperGameStatus.IN_PROCESS);
        }

        if (field.isBomb(x, y) && !field.isMarked(x, y)) {
            setStatus(SapperGameStatus.LOOSE);
            firePropertyChange(SapperEventType.LOOSE, field.getCellsWhenLoose(x, y));
            return;
        }

        CellData[] openedCells = field.open(x, y);
        if (openedCells.length > 0) {
            firePropertyChange(SapperEventType.CELLS_CHANGED, openedCells);

            if (field.isWin()) {
                setStatus(SapperGameStatus.WIN);
                fireWin();
            }
        }
    }

    public void markCell(int x, int y) {
        field.mark(x, y);
        CellData[] cellData = {field.getCellData(x, y)};
        firePropertyChange(SapperEventType.CELLS_CHANGED, cellData);
        firePropertyChange(SapperEventType.FLAGS_CHANGED, field.getFlags());
    }

    private void setStatus(SapperGameStatus status) {
        this.status = status;
    }

    private boolean isStarted() {
        return status.equals(SapperGameStatus.STARTED);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    private void firePropertyChange(Object oldValue, Object newValue) {
        support.firePropertyChange(new PropertyChangeEvent(this, "", oldValue, newValue));
    }

    private void fireWin() {
        support.firePropertyChange(new PropertyChangeEvent(this, "", SapperEventType.WIN, null));
    }
}
