package ru.gelman.common.observer.events;

import ru.gelman.common.Time;
import ru.gelman.common.dto.CellData;
import ru.gelman.common.dto.PlayerRecordData;

import java.util.List;

public class SapperEvent {
    private final SapperEventType type;
    private final List<CellData> cellData;
    private final int flags;
    private final int side;
    private final Time time;
    private final List<PlayerRecordData> records;
    private final boolean isRecord;

    private final boolean isRecordSaved;

    private SapperEvent(SapperEventBuilder builder) {
        this.type = builder.type;
        this.cellData = builder.cellData;
        this.flags = builder.flags;
        this.side = builder.side;
        this.time = builder.time;
        this.records = builder.records;
        this.isRecord = builder.isRecord;
        this.isRecordSaved = builder.isRecordSaved;
    }

    public static SapperEventBuilder builder() {
        return new SapperEventBuilder();
    }

    public SapperEventType getType() {
        return type;
    }

    public List<CellData> getCellData() {
        return cellData;
    }

    public List<PlayerRecordData> getRecords() {
        return records;
    }

    public int getFlags() {
        return flags;
    }

    public int getSide() {
        return side;
    }

    public Time getTime() {
        return time;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public boolean isRecordSaved() {
        return isRecordSaved;
    }

    public static class SapperEventBuilder {
        private SapperEventType type;
        private List<CellData> cellData;
        private int flags;
        private int side;
        private Time time;
        private List<PlayerRecordData> records;
        private boolean isRecord;
        private boolean isRecordSaved;

        public SapperEventBuilder() {
            type = null;
            cellData = null;
            flags = 0;
            side = 0;
            time = null;
            records = null;
            isRecord = false;
            isRecordSaved = false;
        }

        public SapperEventBuilder setType(SapperEventType type) {
            this.type = type;
            return this;
        }

        public SapperEventBuilder setCellData(List<CellData> cellData) {
            this.cellData = cellData;
            return this;
        }

        public SapperEventBuilder setFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public SapperEventBuilder setSide(int side) {
            this.side = side;
            return this;
        }

        public SapperEventBuilder setTime(Time time) {
            this.time = time;
            return this;
        }

        public SapperEventBuilder setRecordsData(List<PlayerRecordData> records) {
            this.records = records;
            return this;
        }

        public SapperEventBuilder isRecord(boolean isRecord) {
            this.isRecord = isRecord;
            return this;
        }

        public SapperEventBuilder isRecordSaved(boolean isSaved) {
            this.isRecordSaved = isSaved;
            return this;
        }

        public SapperEvent build() {
            return new SapperEvent(this);
        }
    }
}
