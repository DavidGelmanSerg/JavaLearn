package model;

import common.SapperDifficulty;
import common.dto.CellData;
import common.observer.SapperListener;
import common.observer.SapperObserver;
import common.observer.events.SapperEvent;
import common.observer.events.SapperEventFactory;
import model.field.SapperField;
import model.field.SapperFieldFactory;

import java.util.ArrayList;
import java.util.List;

public class SapperModel implements SapperObserver {
    private final List<SapperListener> listeners;
    private final SapperEventFactory eventFactory;
    private SapperDifficulty difficulty;
    private SapperGameStatus status;
    private SapperField field;

    public SapperModel() {
        listeners = new ArrayList<>();
        eventFactory = new SapperEventFactory();
        difficulty = SapperDifficulty.BEGINNER;
        setStatus(SapperGameStatus.NOT_STARTED);
        field = SapperFieldFactory.getField(difficulty);
    }

    public void start() {
        setStatus(SapperGameStatus.STARTED);
        notifyListeners(eventFactory.getStartEvent(field.getSide()));
        notifyListeners(eventFactory.getFlagsChangedEvent(field.getFlags()));
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
            notifyListeners(eventFactory.getLooseEvent(field.getCellsWhenLoose(x, y)));
            //notifyListeners(new SapperEvent(SapperEventType.LOOSE, field.getCellsWhenLoose(x, y)));
            return;
        }

        List<CellData> openedCells = field.open(x, y);
        if (openedCells != null) {
            notifyListeners(eventFactory.getCellsChangedEvent(openedCells));
            if (field.isWin()) {
                setStatus(SapperGameStatus.WIN);
                notifyListeners(eventFactory.getWinEvent());
            }
        }
    }

    public void markCell(int x, int y) {
        field.mark(x, y);
        List<CellData> cellData = new ArrayList<>();
        cellData.add(field.getCellData(x, y));

        notifyListeners(eventFactory.getCellsChangedEvent(cellData));
        notifyListeners(eventFactory.getFlagsChangedEvent(field.getFlags()));
    }

    private void setStatus(SapperGameStatus status) {
        this.status = status;
    }

    private boolean isStarted() {
        return status == SapperGameStatus.STARTED;
    }

    @Override
    public void addSapperListener(SapperListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeSapperListener(SapperListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void clearListenerList() {
        listeners.clear();
    }

    @Override
    public void notifyListeners(SapperEvent event) {
        for (SapperListener l : listeners) {
            l.update(event);
        }
    }
}
