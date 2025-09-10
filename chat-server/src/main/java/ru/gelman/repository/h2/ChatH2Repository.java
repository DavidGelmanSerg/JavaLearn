package ru.gelman.repository.h2;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.PropertyLoader;
import ru.gelman.entity.ChatUser;
import ru.gelman.repository.ChatRepository;

import java.sql.*;
import java.util.Properties;

@Slf4j
public class ChatH2Repository implements ChatRepository {
    private static final Properties QUERIES = PropertyLoader.load("query.properties");
    private final String url;
    private final String login;
    private final String password;

    public ChatH2Repository() {
        log.debug("initializing h2 database");
        Properties config = PropertyLoader.load("db_config.properties");
        url = config.getProperty("url", "");
        login = config.getProperty("login", "");
        password = config.getProperty("password", "");

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {

            String createUsersTableQuery = getQuery("create_users_table");
            log.debug("executing query: {}", createUsersTableQuery);
            statement.executeUpdate(createUsersTableQuery);

            String createMessagesTableQuery = getQuery("create_messages_table");
            log.debug("executing query: {}", createMessagesTableQuery);
            statement.executeUpdate(createMessagesTableQuery);

            String createChatsTableQuery = getQuery("create_chats_table");
            log.debug("executing query: {}", createChatsTableQuery);
            statement.executeUpdate(createChatsTableQuery);

            String createChatsUsersTableQuery = getQuery("create_users_chats_table");
            log.debug("executing query: {}", createChatsUsersTableQuery);
            statement.executeUpdate(createChatsUsersTableQuery);

        } catch (SQLException e) {
            log.error("failed to initialize h2 database");
            throw new RuntimeException("App could not connect to database!", e);
        }
    }

    @Override
    public boolean has(ChatUser user) {
        if (user.getId() != null) {
            log.debug("checking existing user. id: {}", user.getId());
            return existsById(user.getId(), getQuery("exists_user_id"));
        } else {
            log.debug("checking existing user by name: {}", user.getName());
            return hasUser(user.getName());
        }
    }

    @Override
    public boolean hasUser(String name) {
        try (Connection connection = getConnection()) {
            PreparedStatement existsUserQuery = connection.prepareStatement(getQuery("exists_user_name"));
            existsUserQuery.setString(1, name);
            log.debug("executing exists user query. name: {}", name);
            ResultSet rs = existsUserQuery.executeQuery();

            boolean result = rs.next() && rs.getInt(1) > 0;
            log.debug("execution result: {}", result);
            return result;
        } catch (SQLException e) {
            log.error("Database error occurred while executing exists user query. name: {}", name);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasUser(int id) {
        log.debug("checking existence of user. id: {}", id);
        return existsById(id, getQuery("exists_user_id"));
    }

    @Override
    public void save(ChatUser user) {
        try (Connection connection = getConnection()) {
            PreparedStatement saveUserQuery = connection.prepareStatement(getQuery("insert_user"),
                    Statement.RETURN_GENERATED_KEYS);
            saveUserQuery.setString(2, user.getName());
            saveUserQuery.setString(3, user.getPassword());

            log.debug("insert user {} to database", user);
            if (saveUserQuery.executeUpdate() <= 0) {
                log.error("insert user with name {} failed", user.getName());
                throw new RuntimeException("create user failed");
            }

            log.debug("successfully insert user {}", user);
        } catch (SQLException e) {
            log.error("Database error occurred while inserting user: {}", user);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChatUser getUser(String name) {
        try (Connection connection = getConnection()) {
            PreparedStatement getUserQuery = connection.prepareStatement(getQuery("select_user_by_name"));
            getUserQuery.setString(1, name);
            log.debug("executing select user query. name: {}", name);
            ResultSet rs = getUserQuery.executeQuery();
            if (!rs.next()) {
                log.warn("user with name {} not found:", name);
                throw new RuntimeException("User not found");
            }
            ChatUser user = mapToUser(rs);
            log.debug("found user: {}", user);
            return user;
        } catch (SQLException e) {
            log.error("Database error occurred while selecting user: {}", name);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChatUser getUser(int id) {
        try (Connection connection = getConnection()) {
            PreparedStatement getUserQuery = connection.prepareStatement(getQuery("select_user_by_id"));
            getUserQuery.setInt(1, id);
            log.debug("executing select user query. id: {}", id);
            ResultSet rs = getUserQuery.executeQuery();
            if (!rs.next()) {
                log.warn("user with id {} not found:", id);
                throw new RuntimeException("User not found");
            }
            ChatUser user = mapToUser(rs);
            log.debug("found user: {}", user);
            return user;
        } catch (SQLException e) {
            log.error("Database error occurred while selecting user: {}", id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean login(String name, String password) {
        try (Connection connection = getConnection()) {
            PreparedStatement loginQuery = connection.prepareStatement(getQuery("select_user_login"));
            loginQuery.setString(1, name);
            loginQuery.setString(2, password);
            log.debug("executing login user query. name: {}", name);
            ResultSet rs = loginQuery.executeQuery();

            boolean result = rs.next() && rs.getInt(1) > 0;
            log.debug("execution result: {}", result);
            return result;
        } catch (SQLException e) {
            log.error("Database error occurred while login user: {}", name);
            throw new RuntimeException(e);
        }
    }

    private boolean existsById(Integer id, String query) {
        try (Connection connection = getConnection()) {
            PreparedStatement existsQuery = connection.prepareStatement(query);
            existsQuery.setInt(1, id);
            log.debug("executing exists by id query. id: {}", id);
            ResultSet rs = existsQuery.executeQuery();

            boolean result = rs.next() && rs.getInt(1) > 0;
            log.debug("execution result: {}", result);
            return result;
        } catch (SQLException e) {
            log.error("Database error occurred while executing exists by id query. id: {}", id);
            throw new RuntimeException(e);
        }
    }

    private Connection getConnection() throws SQLException {
        log.debug("open new connection for database user: {}", login);
        return DriverManager.getConnection(url, login, password);
    }

    private String getQuery(String key) {
        String query = QUERIES.getProperty(key, "");
        if (query.isEmpty()) {
            log.error("query {} not found", key);
            throw new RuntimeException("cannot find query " + key);
        }
        return query;
    }

    private ChatUser mapToUser(ResultSet rs) throws SQLException {
        log.debug("mapping result set to user");
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        return ChatUser.existingUser(id, name, password);
    }

}
