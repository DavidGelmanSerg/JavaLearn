package ru.gelman.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatSession {
    private final String sessionId;
    private final ChatUser user;
    private LocalDateTime expiredDate;

    public static ChatSession from(String sessionId, ChatUser user, LocalDateTime expiredDate) {
        ChatSession session = new ChatSession(sessionId, user);
        session.setExpiredDate(expiredDate);
        return session;
    }
}
