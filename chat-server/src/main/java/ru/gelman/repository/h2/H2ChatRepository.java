package ru.gelman.repository.h2;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.entity.ChatUser;
import ru.gelman.entity.chat.Chat;
import ru.gelman.entity.chat.ChatInfo;
import ru.gelman.entity.message.ChatMessage;
import ru.gelman.mapper.JdbcResultSetMapper;
import ru.gelman.repository.ChatRepository;
import ru.gelman.repository.config.DataBaseConfig;
import ru.gelman.repository.jdbc.JdbcExecutor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class H2ChatRepository implements ChatRepository {
    private final JdbcExecutor jdbcExecutor;

    public H2ChatRepository(DataBaseConfig repositoryConfig) {
        jdbcExecutor = new JdbcExecutor(repositoryConfig);
    }

    @Override
    public Chat save(Chat chat) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement saveChatInfoQuery = connection.prepareStatement(
                    "INSERT INTO Chats (name, creatorId, deleted) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS)
            ) {
                connection.setAutoCommit(false);
                log.debug("executing insert chat info query. chat: {}", chat);
                saveChatInfoQuery.setString(1, chat.getInfo().getName());
                saveChatInfoQuery.setInt(2, chat.getInfo().getCreatorId());
                saveChatInfoQuery.setBoolean(3, chat.getInfo().isDeleted());

                if (saveChatInfoQuery.executeUpdate() <= 0) {
                    connection.rollback();
                    log.warn("inserting chat info failed. transaction rollback");
                    throw new RuntimeException("inserting chat failed");
                }

                ResultSet keys = saveChatInfoQuery.getGeneratedKeys();
                if (!keys.next()) {
                    connection.rollback();
                    log.warn("inserting chat info failed. auto generated key not found. connection rollback");
                    throw new RuntimeException("inserting chat failed");
                }

                int chatId = keys.getInt(1);
                chat.getInfo().setId(chatId);

                PreparedStatement linkUserToChatQuery = connection.prepareStatement("INSERT INTO Users_Chats VALUES (?, ?);");
                for (ChatUser user : chat.getUsers()) {
                    linkUserToChatQuery.setInt(1, user.getId());
                    linkUserToChatQuery.setInt(2, chatId);

                    log.debug("linking user {} to chat {}", user, chat);
                    int userLinked = linkUserToChatQuery.executeUpdate();
                    if (userLinked <= 0) {
                        connection.rollback();
                        log.warn("linking user {} to chat {} failed. transaction rollback", user, chat);
                        throw new RuntimeException("inserting chat failed");
                    }
                }
                connection.commit();
                return chat;
            } catch (SQLException e) {
                log.error("database error occurred while saving chat: {}", chat);
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public List<ChatInfo> getUserChatsInfo(ChatUser user) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement selectChatInfoQuery = connection.prepareStatement("SELECT * FROM Chats JOIN Users_Chats ON Chats.creatorId = ?;")) {
                selectChatInfoQuery.setInt(1, user.getId());

                log.debug("executing select chats info for user: {}", user);
                ResultSet chatInfosRs = selectChatInfoQuery.executeQuery();
                List<ChatInfo> chatInfos = new ArrayList<>();
                while (chatInfosRs.next()) {
                    ChatInfo info = JdbcResultSetMapper.toChatInfoEntity(chatInfosRs);
                    log.debug("adding info {} to list", info);
                    chatInfos.add(info);
                }
                log.debug("successfully get {} chat infos from database", chatInfos.size());
                return chatInfos;
            } catch (SQLException e) {
                log.error("database error occurred while selecting chat infos with for user: {}", user);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public ChatInfo getChatInfo(int chatId) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement getChatInfoQuery = connection.prepareStatement("SELECT * FROM Chats WHERE id = ?")) {
                getChatInfoQuery.setInt(1, chatId);
                log.debug("executing select chat info query. id: {}", chatId);
                ResultSet rs = getChatInfoQuery.executeQuery();
                if (!rs.next()) {
                    log.warn("chat with id {} not found:", chatId);
                    throw new RuntimeException("chat not found");
                }
                ChatInfo chatInfo = JdbcResultSetMapper.toChatInfoEntity(rs);
                log.debug("found chat: {}", chatInfo);
                return chatInfo;
            } catch (SQLException e) {
                log.error("database error occurred while selecting chat info by id: {}", chatId);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<ChatUser> getChatUsers(int chatId) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement selectChatUsersQuery = connection.prepareStatement("SELECT * FROM Users JOIN Users_Chats ON Users_Chats.chatId = ?")) {
                selectChatUsersQuery.setInt(1, chatId);

                log.debug("executing select users for chat: {}", chatId);
                ResultSet usersRs = selectChatUsersQuery.executeQuery();
                List<ChatUser> users = new ArrayList<>();
                while (usersRs.next()) {
                    users.add(JdbcResultSetMapper.toUserEntity(usersRs));
                }
                log.debug("successfully get {} users from database", users.size());
                return users;
            } catch (SQLException e) {
                log.error("database error occurred while selecting users for chat: {}", chatId);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public ChatMessage save(ChatMessage message) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement saveMessageQuery = connection.prepareStatement(
                    "INSERT INTO Messages (content, creationDateTime, creatorId, chatId, deleted) VALUES (?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS)
            ) {

                saveMessageQuery.setString(1, message.getContent());
                saveMessageQuery.setObject(2, message.getCreationDateTime());
                saveMessageQuery.setInt(3, message.getCreator().getId());
                saveMessageQuery.setInt(4, message.getChatId());

                log.debug("executing insert message query. message: {}", message);
                int effectedRows = saveMessageQuery.executeUpdate();
                if (effectedRows <= 0) {
                    log.warn("insert message query failed. no message was saved");
                    throw new RuntimeException("insert message query failed");
                }

                ResultSet keys = saveMessageQuery.getGeneratedKeys();
                if (!keys.next()) {
                    log.warn("insert message query failed. no key was generated for message: {}", message);
                    throw new RuntimeException("insert message query failed");
                }

                message.setId(keys.getInt(1));
                log.debug("successfully saved message with id: {}", message.getId());
                return message;
            } catch (SQLException e) {
                log.error("database error occurred while saving message: {}", message);
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<ChatMessage> getLastMessages(int chatId, int messagesLimit) {
        return jdbcExecutor.execute(connection -> {
            try (PreparedStatement selectMessagesQuery = connection.prepareStatement("SELECT * FROM Messages WHERE chatId = ? ORDER BY creationDateTime DESC LIMIT ?;")) {
                selectMessagesQuery.setInt(1, chatId);
                selectMessagesQuery.setInt(2, messagesLimit);

                log.debug("executing select last {} messages for chat: {}", messagesLimit, chatId);
                ResultSet messagesRs = selectMessagesQuery.executeQuery();
                List<ChatMessage> messages = new ArrayList<>();
                while (messagesRs.next()) {
                    ChatMessage message = JdbcResultSetMapper.toMessageEntity(messagesRs);
                    log.debug("adding message: {} to list", message);
                    messages.add(message);
                }
                log.debug("successfully got {} messages from database", messages.size());
                return messages;
            } catch (SQLException e) {
                log.error("database error occurred while selecting messages for chat: {}", chatId);
                throw new RuntimeException(e);
            }
        });
    }
}
