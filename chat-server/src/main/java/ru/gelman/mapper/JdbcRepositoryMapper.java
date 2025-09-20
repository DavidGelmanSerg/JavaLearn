package ru.gelman.mapper;

import ru.gelman.entity.ChatInfo;
import ru.gelman.entity.ChatUser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRepositoryMapper {
    public static ChatUser toUserEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        return ServiceMapper.toUser(id, name, password);
    }

    public static ChatInfo toChatInfoEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        int creatorId = rs.getInt("creatorId");
        boolean deleted = rs.getBoolean("deleted");
        return ServiceMapper.toChatInfo(id, name, creatorId, deleted);
    }
}
