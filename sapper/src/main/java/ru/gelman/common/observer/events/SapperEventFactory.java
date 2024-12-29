package ru.gelman.common.observer.events;

import ru.gelman.common.Time;
import ru.gelman.common.dto.CellData;
import ru.gelman.common.dto.PlayerRecordData;

import java.util.List;

public class SapperEventFactory {
    public SapperEvent getInitEvent(int side, int flags) {
        return SapperEvent.builder()
                .setType(SapperEventType.INIT)
                .setSide(side)
                .setFlags(flags)
                .build();
    }

    public SapperEvent getStartEvent() {
        return SapperEvent.builder()
                .setType(SapperEventType.START)
                .build();
    }

    public SapperEvent getCellsChangedEvent(List<CellData> cellData) {
        return SapperEvent.builder()
                .setType(SapperEventType.CELLS_CHANGED)
                .setCellData(cellData)
                .build();
    }

    public SapperEvent getFlagsChangedEvent(int flags) {
        return SapperEvent.builder()
                .setType(SapperEventType.FLAGS_CHANGED)
                .setFlags(flags)
                .build();
    }

    public SapperEvent getTimeChangedEvent(Time time) {
        return SapperEvent.builder()
                .setType(SapperEventType.TIME_CHANGED)
                .setTime(time)
                .build();
    }

    public SapperEvent getLooseEvent() {
        return SapperEvent.builder()
                .setType(SapperEventType.LOOSE)
                .build();
    }

    public SapperEvent getWinEvent(boolean isRecord) {
        return SapperEvent.builder()
                .setType(SapperEventType.WIN)
                .isRecord(isRecord)
                .build();
    }

    public SapperEvent getSelectedPlayersEvent(List<PlayerRecordData> records) {
        return SapperEvent.builder()
                .setType(SapperEventType.RECORDS_SELECTED)
                .setRecordsData(records)
                .build();
    }

    public SapperEvent getSaveRecordEvent(boolean isSaved) {
        return SapperEvent.builder()
                .setType(SapperEventType.SAVE_RECORD)
                .isRecordSaved(isSaved)
                .build();
    }
}
