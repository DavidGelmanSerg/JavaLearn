package ru.gelman.repository.h2;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;
import ru.gelman.mapper.JdbcResultSetMapper;
import ru.gelman.repository.SessionRepository;
import ru.gelman.repository.config.DataBaseConfig;
import ru.gelman.repository.jdbc.JdbcExecutor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class H2SessionRepository implements SessionRepository {
    private final JdbcExecutor jdbcExecutor;

    public H2SessionRepository(DataBaseConfig repositoryConfig) {
        jdbcExecutor = new JdbcExecutor(repositoryConfig);
    }

    @Override
    public void save(ChatSession session) {
        jdbcExecutor.execute(connection -> {
            try (PreparedStatement saveSessionQuery = connection.prepareStatement("INSERT INTO Sessions Values(?, ?, ?);")) {
                saveSessionQuery.setString(1, session.getSessionId());
                saveSessionQuery.setInt(2, session.getUser().getId());
                saveSessionQuery.setObject(3, session.getExpiredDate());

                log.debug("executing insert session query. session: {}", session);
                int effectedRows = saveSessionQuery.executeUpdate();
                if (effectedRows <= 0) {
                    log.warn("insert session query failed");
                    throw new RuntimeException("insert session query failed");
                }

                log.debug("successfully saved session: {}", session);
            } catch (SQLException e) {
                log.error("database error occurred while saving session: {}", session);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public ChatSession getSession(String sessionId) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement getSessionQuery = connection.prepareStatement("SELECT * FROM Sessions INNER JOIN Users ON Sessions.userId = Users.id WHERE Sessions.sessionId = ?")) {
                getSessionQuery.setString(1, sessionId);
                log.debug("executing select session query: {}", getSessionQuery);
                ResultSet rs = getSessionQuery.executeQuery();
                if (!rs.next()) {
                    log.warn("session with id {} not found:", sessionId);
                    throw new RuntimeException("session not found");
                }

                ChatSession session = JdbcResultSetMapper.toSessionEntity(rs);
                log.debug("found session: {}", session);
                return session;
            } catch (SQLException e) {
                log.error("database error occurred while selecting session with id: {}", sessionId);
                log.error("error: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateSessionExpiredDate(ChatSession session) {
        jdbcExecutor.execute(connection -> {
            try (PreparedStatement query = connection.prepareStatement("UPDATE Sessions SET expiredDate = ? WHERE sessionId = ?")) {
                query.setObject(1, session.getExpiredDate());
                query.setString(2, session.getSessionId());

                log.debug("executing update session expired date query. session: {}", session);
                int updatedRows = query.executeUpdate();
                if (updatedRows < 0) {
                    log.warn("updated session expired dated failed");
                    throw new RuntimeException("updated session expired dated failed");
                }
            } catch (SQLException e) {
                log.error("database error occurred while updating session: {}", session);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<ChatSession> getSessionsAfter(LocalDateTime timestamp) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement selectSessionsQuery = connection.prepareStatement("SELECT * FROM Sessions INNER JOIN Users ON Sessions.userId = Users.id WHERE expiredDate > ?;")) {
                selectSessionsQuery.setTimestamp(1, Timestamp.valueOf(timestamp));

                log.debug("executing select sessions query with expired date before: {}", timestamp);
                ResultSet sessionsRs = selectSessionsQuery.executeQuery();
                List<ChatSession> sessions = new ArrayList<>();
                while (sessionsRs.next()) {
                    ChatSession session = JdbcResultSetMapper.toSessionEntity(sessionsRs);
                    log.debug("adding session {} to list", session);
                    sessions.add(session);
                }
                log.debug("successfully get {} sessions from database", sessions.size());
                return sessions;
            } catch (SQLException e) {
                log.error("database error occurred while selecting sessions with expired date before: {}", timestamp);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public boolean login(ChatUser user) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement loginQuery = connection.prepareStatement("SELECT COUNT(*) FROM Users WHERE name = ? and password = ?;")) {
                loginQuery.setString(1, user.getName());
                loginQuery.setString(2, user.getPassword());
                log.debug("executing login user query. name: {}", user.getName());
                ResultSet rs = loginQuery.executeQuery();

                boolean result = rs.next() && rs.getInt(1) > 0;
                log.debug("execution result: {}", result);
                return result;
            } catch (SQLException e) {
                log.error("database error occurred while login user: {}", user.getPassword());
                return false;
            }
        });
    }
}
