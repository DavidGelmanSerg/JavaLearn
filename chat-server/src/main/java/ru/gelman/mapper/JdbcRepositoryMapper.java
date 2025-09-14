package ru.gelman.mapper;

import ru.gelman.entity.ChatEntityFactory;
import ru.gelman.entity.ChatUser;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcRepositoryMapper {
    public static ChatUser toUserEntity(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String password = rs.getString("password");
        return ChatEntityFactory.existingUser(id, name, password);
    }
}
