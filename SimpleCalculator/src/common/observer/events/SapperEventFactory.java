package common.observer.events;

import common.dto.CellData;

import java.util.List;

public class SapperEventFactory {
    public SapperEvent getStartEvent(int side) {
        return new SapperEvent(SapperEventType.START, null, 0, side);
    }

    public SapperEvent getCellsChangedEvent(List<CellData> cellData) {
        return new SapperEvent(SapperEventType.CELLS_CHANGED, cellData, 0, 0);
    }

    public SapperEvent getFlagsChangedEvent(int flags) {
        return new SapperEvent(SapperEventType.FLAGS_CHANGED, null, flags, 0);
    }

    public SapperEvent getLooseEvent(List<CellData> looseCells) {
        return new SapperEvent(SapperEventType.LOOSE, looseCells, 0, 0);
    }

    public SapperEvent getWinEvent() {
        return new SapperEvent(SapperEventType.WIN, null, 0, 0);
    }
}
