package ru.gelman.mapper;

import ru.gelman.dto.SessionData;
import ru.gelman.dto.UserData;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;

public class ServiceMapper {

    public UserData toUserDto(ChatUser user) {
        return new UserData(user.getId(), user.getName());
    }

    public SessionData toSessionDto(ChatSession session) {
        return new SessionData(toUserDto(session.getUser()), session.getSessionId());
    }
}
