package ru.gelman.repository.jdbc;

import lombok.RequiredArgsConstructor;
import ru.gelman.repository.config.DataBaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;

@RequiredArgsConstructor
public class JdbcExecutor {
    private final DataBaseConfig config;

    public <T> T execute(Function<Connection, T> queryFunction) {
        try (Connection connection = getConnection()) {
            return queryFunction.apply(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(Consumer<Connection> action) {
        try (Connection connection = getConnection()) {
            action.accept(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
    }
}
