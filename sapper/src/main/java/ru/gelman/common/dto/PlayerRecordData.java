package ru.gelman.common.dto;

import java.util.Objects;

public record PlayerRecordData(String playerName, String difficulty,
                               String points) implements Comparable<PlayerRecordData> {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerRecordData that = (PlayerRecordData) o;
        return Objects.equals(playerName, that.playerName) && Objects.equals(difficulty, that.difficulty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, difficulty);
    }

    @Override
    public int compareTo(PlayerRecordData o) {
        return points.compareTo(o.points);
    }

    @Override
    public String toString() {
        return playerName + " (" + difficulty + "): " + points;
    }
}