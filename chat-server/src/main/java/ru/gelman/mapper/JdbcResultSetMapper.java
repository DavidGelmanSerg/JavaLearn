package ru.gelman.mapper;

import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;
import ru.gelman.entity.chat.ChatInfo;
import ru.gelman.entity.message.ChatMessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class JdbcResultSetMapper {

    public static ChatUser toUserEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        boolean deleted = rs.getBoolean("deleted");
        boolean online = rs.getBoolean("online");
        return ChatUser.existing(id, name, password, deleted, online);
    }

    public static ChatSession toSessionEntity(ResultSet rs) throws SQLException {
        String sessionId = rs.getString("sessionId");
        LocalDateTime expired = rs.getObject("expiredDate", LocalDateTime.class);
        ChatUser user = toUserEntity(rs);
        return ChatSession.from(sessionId, user, expired);
    }

    public static ChatInfo toChatInfoEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int creator = rs.getInt("creatorId");
        String name = rs.getString("name");
        boolean deleted = rs.getBoolean("deleted");
        return ChatInfo.ofExistingChat(id, creator, name, deleted);
    }

    public static ChatMessage toMessageEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int chatId = rs.getInt("chatId");
        ChatUser creator = toUserEntity(rs);
        String content = rs.getString("content");
        LocalDateTime creationDateTime = rs.getObject("creationDateTime", LocalDateTime.class);
        boolean deleted = rs.getBoolean("deleted");
        return ChatMessage.existing(chatId, creator, creationDateTime, content, deleted, id);

    }
}
