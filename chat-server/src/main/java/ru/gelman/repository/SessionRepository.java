package ru.gelman.repository;

import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository {
    void save(ChatSession session);

    ChatSession getSession(String sessionId);

    void updateSessionExpiredDate(ChatSession session);

    List<ChatSession> getSessionsAfter(LocalDateTime timestamp);

    boolean login(ChatUser user);
}
