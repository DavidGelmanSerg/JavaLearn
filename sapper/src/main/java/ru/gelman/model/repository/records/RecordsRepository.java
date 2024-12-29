package ru.gelman.model.repository.records;

import ru.gelman.common.SapperDifficulty;
import ru.gelman.model.repository.records.h2.PlayerRecord;

import java.util.List;

public interface RecordsRepository {
    List<PlayerRecord> getRecords(SapperDifficulty difficulty);

    PlayerRecord getRecord(String playerName, SapperDifficulty difficulty);

    boolean add(PlayerRecord record);

    boolean update(PlayerRecord record);
}
