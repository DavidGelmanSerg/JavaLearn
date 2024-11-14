package model.repository.records;

import common.SapperDifficulty;
import model.repository.records.h2.PlayerRecord;

import java.util.List;

public interface RecordsRepository {
    List<PlayerRecord> getRecords(SapperDifficulty difficulty);

    PlayerRecord getRecord(String playerName, SapperDifficulty difficulty);

    boolean add(PlayerRecord record);

    boolean update(PlayerRecord record);
}
