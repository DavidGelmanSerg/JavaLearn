package model.repository.records.h2;

import common.SapperDifficulty;
import common.Time;
import model.repository.records.RecordsRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class H2Repository implements RecordsRepository {
    private static final String URL = "jdbc:h2:file:./src/model/repository/records/h2/db/db;";
    private static final String LOGIN = "sa";
    private static final String PASSWORD = "";
    private static final String INSERT_QUERY_PATTERN = "INSERT INTO player_records VALUES ('%s', '%s', '%s');";
    private static final String UPDATE_QUERY_PATTERN = "UPDATE player_records SET time = '%s' WHERE player_name = '%s' AND difficulty = '%s';";

    public H2Repository() {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD); Statement statement = connection.createStatement()) {
            String createRecordsTableQuery = "CREATE TABLE IF NOT EXISTS player_records (" + "player_name VARCHAR(50), " + "difficulty VARCHAR(20), " + "time VARCHAR(5));";
            statement.executeUpdate(createRecordsTableQuery);
        } catch (SQLException e) {
            throw new RuntimeException("App could not connect to database!", e);
        }
    }

    @Override
    public List<PlayerRecord> getRecords(SapperDifficulty difficulty) {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * FROM player_records WHERE difficulty= '%s'", difficulty.toString().toLowerCase());
            ResultSet rs = statement.executeQuery(query);
            List<PlayerRecord> recordsSet = new ArrayList<>();

            while (rs.next()) {
                String currentPlayerName = rs.getString("player_name");
                SapperDifficulty currentDifficulty = SapperDifficulty.valueOf(rs.getString("difficulty").toUpperCase());
                Time time = Time.of(rs.getString("time"));

                recordsSet.add(new PlayerRecord(currentPlayerName, currentDifficulty, time));
            }
            return recordsSet;

        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public PlayerRecord getRecord(String playerName, SapperDifficulty difficulty) {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD); Statement statement = connection.createStatement()) {
            String query = String.format("SELECT * FROM player_records WHERE player_Name = '%s' AND difficulty= '%s'", playerName, difficulty.toString().toLowerCase());
            ResultSet rs = statement.executeQuery(query);
            PlayerRecord record = null;
            if (rs.next()) {
                String recordPlayerName = rs.getString("player_name");
                SapperDifficulty recordDifficulty = SapperDifficulty.valueOf(rs.getString("difficulty").toUpperCase());
                Time recordTime = Time.of(rs.getString("time"));
                record = new PlayerRecord(recordPlayerName, recordDifficulty, recordTime);
            }
            return record;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public boolean add(PlayerRecord record) {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement statement = connection.createStatement()) {
            String playerName = record.getPlayerName();
            String difficulty = record.getDifficulty().toString().toLowerCase();
            Time time = record.getPoints();

            int effectedRows = statement.executeUpdate(String.format(INSERT_QUERY_PATTERN, playerName, difficulty, time));
            return effectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean update(PlayerRecord record) {
        try (Connection connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
             Statement statement = connection.createStatement()) {
            String playerName = record.getPlayerName();
            String difficulty = record.getDifficulty().toString().toLowerCase();
            Time time = record.getPoints();

            int effectedRows = statement.executeUpdate(String.format(UPDATE_QUERY_PATTERN, time, playerName, difficulty));
            return effectedRows > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
