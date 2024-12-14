package ru.gelman.model.repository.records.h2;

import ru.gelman.common.SapperDifficulty;
import ru.gelman.common.Time;
import ru.gelman.common.dto.PlayerRecordData;

import java.util.List;
import java.util.Objects;

public class PlayerRecord implements Comparable<PlayerRecord> {
    private final String playerName;
    private final SapperDifficulty difficulty;
    private Time points;

    public PlayerRecord(String playerName, SapperDifficulty difficulty, Time points) {
        this.playerName = playerName;
        this.difficulty = difficulty;
        this.points = points;
    }

    public static List<PlayerRecordData> getPlayerRecordsData(List<PlayerRecord> records) {
        return records.stream().map(record -> {
            String difficulty = record.difficulty.toString().toLowerCase();
            String points = record.points.toString();
            return new PlayerRecordData(record.playerName, difficulty, points);
        }).toList();
    }

    public Time getPoints() {
        return points;
    }

    public void setTime(Time time) {
        this.points = time;
    }

    public SapperDifficulty getDifficulty() {
        return difficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerRecord that = (PlayerRecord) o;
        return Objects.equals(playerName, that.playerName) && difficulty == that.difficulty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, difficulty);
    }

    @Override
    public int compareTo(PlayerRecord o) {
        return points.compareTo(o.points);
    }

    @Override
    public String toString() {
        return playerName + " (" + difficulty.toString().toLowerCase() + "): " + points;
    }
}
