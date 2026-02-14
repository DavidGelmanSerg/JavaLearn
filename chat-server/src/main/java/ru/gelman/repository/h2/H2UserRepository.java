package ru.gelman.repository.h2;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.entity.ChatUser;
import ru.gelman.mapper.JdbcResultSetMapper;
import ru.gelman.repository.UserRepository;
import ru.gelman.repository.config.DataBaseConfig;
import ru.gelman.repository.jdbc.JdbcExecutor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class H2UserRepository implements UserRepository {
    private final JdbcExecutor jdbcExecutor;

    public H2UserRepository(DataBaseConfig repositoryConfig) {
        jdbcExecutor = new JdbcExecutor(repositoryConfig);
    }

    @Override
    public boolean has(ChatUser user) {
        if (user.getId() != null) {
            log.debug("checking existing user. id: {}", user.getId());
            return has(user.getId());
        } else {
            log.debug("checking existing user by name: {}", user.getName());
            return has(user.getName());
        }
    }

    @Override
    public boolean has(int id) {
        return jdbcExecutor.execute(connection -> {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE id = ?;");
                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();

                boolean result = rs.next() && rs.getInt(1) > 0;
                log.debug("user with id {} exists: {}", id, result);
                return result;
            } catch (SQLException e) {
                log.error("Database error occurred while executing exists user query. id: {}", id);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public boolean has(String name) {
        return jdbcExecutor.execute(connection -> {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE name = ?;");
                statement.setString(1, name);
                log.debug("executing exists user query. name: {}", name);
                ResultSet rs = statement.executeQuery();

                boolean result = rs.next() && rs.getInt(1) > 0;
                log.debug("user with name {} exists: {}", name, result);
                return result;
            } catch (SQLException e) {
                log.error("Database error occurred while executing exists user query. name: {}", name);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public ChatUser save(ChatUser user) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement saveUserQuery = connection.prepareStatement(
                    "INSERT INTO Users (name, password, deleted) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            )) {

                saveUserQuery.setString(1, user.getName());
                saveUserQuery.setString(2, user.getPassword());
                saveUserQuery.setBoolean(3, user.isDeleted());

                log.debug("insert user {} to database", user);
                if (saveUserQuery.executeUpdate() > 0) {
                    ResultSet keys = saveUserQuery.getGeneratedKeys();
                    if (keys.next()) {
                        int id = saveUserQuery.getGeneratedKeys().getInt(1);
                        user.setId(id);
                        log.debug("successfully insert user {}", user);
                        return user;
                    }
                }
                log.warn("insert user with name {} failed", user.getName());
                throw new RuntimeException("create user failed");
            } catch (SQLException e) {
                log.error("Database error occurred while inserting user: {}", user);
                throw new RuntimeException(e);
            }
        });
    }


    @Override
    public ChatUser getUser(String name) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement getUserQuery = connection.prepareStatement("SELECT * FROM Users WHERE name = ?;")) {
                getUserQuery.setString(1, name);
                log.debug("executing select user query. name: {}", name);
                ResultSet rs = getUserQuery.executeQuery();
                if (!rs.next()) {
                    log.warn("user with name {} not found:", name);
                    throw new RuntimeException("User not found");
                }
                ChatUser user = JdbcResultSetMapper.toUserEntity(rs);
                log.debug("found user: {}", user);
                return user;
            } catch (SQLException e) {
                log.error("Database error occurred while selecting user: {}", name);
                throw new RuntimeException(e);
            }

        });
    }

    @Override
    public ChatUser getUser(int id) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement getUserQuery = connection.prepareStatement("SELECT * FROM Users WHERE id = ?;")) {
                getUserQuery.setInt(1, id);
                log.debug("executing select user query. id: {}", id);
                ResultSet rs = getUserQuery.executeQuery();
                if (!rs.next()) {
                    log.warn("user with id {} not found:", id);
                    throw new RuntimeException("User not found");
                }
                ChatUser user = JdbcResultSetMapper.toUserEntity(rs);
                log.debug("found user: {}", user);
                return user;
            } catch (SQLException e) {
                log.error("Database error occurred while selecting user: {}", id);
                throw new RuntimeException(e);
            }
        });
    }
}
