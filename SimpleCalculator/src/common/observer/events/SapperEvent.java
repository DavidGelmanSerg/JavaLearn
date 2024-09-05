package common.observer.events;

import common.dto.CellData;

import java.util.List;

public record SapperEvent(SapperEventType type, List<CellData> cellData, int flags, int side) {
}
