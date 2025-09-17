package ru.gelman.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatEntityFactory {
    public static ChatUser newUser(String name, String password) {
        return new ChatUser(null, name, password, false);
    }

    public static ChatUser existingUser(Integer id, String name, String password) {
        return new ChatUser(id, name, password, false);
    }

    public static Chat newChat(String name, int creatorId, List<ChatUser> users) {
        ChatInfo info = new ChatInfo(creatorId, null, name, false);
        return new Chat(info, users, List.of());
    }

    public static Chat existingChat(ChatInfo info, List<ChatMessage> messages, List<ChatUser> users) {
        return new Chat(info, users, messages);
    }

    public static ChatMessage newMessage(int chatId, int creatorId, String content, String creationDateTime) {
        LocalDateTime messageDateTime = LocalDateTime.parse(creationDateTime, DateTimeFormatter.ISO_DATE_TIME);
        return new ChatMessage(chatId, creatorId, messageDateTime, null, content, false);
    }

    public static ChatMessage existingMessage(int id, int chatId, int creatorId, String content, LocalDateTime creationDateTime) {
        return new ChatMessage(chatId, creatorId, creationDateTime, id, content, false);
    }

    public static ChatSession newSession(String sessionId, ChatUser user, int sessionTimeLive, TimeUnit unit) {
        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(unit.toMinutes(sessionTimeLive));
        return new ChatSession(sessionId, user, expiredDate);
    }

    public static ChatSession existingSession(String sessionId, ChatUser user, LocalDateTime expired) {
        return new ChatSession(sessionId, user, expired);
    }
}
