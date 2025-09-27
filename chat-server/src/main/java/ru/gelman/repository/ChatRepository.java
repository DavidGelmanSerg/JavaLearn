package ru.gelman.repository;


import ru.gelman.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository {

    boolean has(ChatUser user);

    boolean hasUser(int id);

    ChatUser save(ChatUser user);

    ChatUser getUser(String name);

    ChatUser getUser(int id);

    boolean login(ChatUser user);

    boolean hasUser(String name);

    Chat save(Chat chat);

    ChatMessage save(ChatMessage message);

    void save(ChatSession session);

    ChatSession getSession(String sessionId);

    void updateSessionExpiredDate(ChatSession session);

    List<ChatSession> getSessionsAfter(LocalDateTime timestamp);

    List<ChatInfo> getUserChatsInfo(ChatUser user);

    List<ChatMessage> getLastMessages(ChatInfo chat, int messagesLimit);

    ChatInfo getChatInfo(int chatId);

    List<ChatUser> getChatUsers(ChatInfo chat);
}
