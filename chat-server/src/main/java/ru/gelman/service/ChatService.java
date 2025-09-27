package ru.gelman.service;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.PropertyLoader;
import ru.gelman.entity.*;
import ru.gelman.mapper.ServiceMapper;
import ru.gelman.repository.ChatRepository;
import ru.gelman.service.exception.LoginFailedException;
import ru.gelman.service.exception.SessionInactiveException;
import ru.gelman.service.exception.UserAlreadyExistsException;
import ru.gelman.service.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ChatService {
    private static final Properties SERVICE_CONFIG = PropertyLoader.load("/service_config.properties");
    private final ChatRepository repository;

    public ChatService(ChatRepository repository) {
        this.repository = repository;
    }

    private void checkSession(String sessionId) {
        log.debug("checking session: {}", sessionId);
        ChatSession session = repository.getSession(sessionId);
        if (LocalDateTime.now().isAfter(session.getExpiredDate())) {
            log.debug("session is inactive. id: {}", sessionId);
            throw new SessionInactiveException(sessionId);
        }
        log.debug("session {} exists and is active", session);
    }

    public ChatUser createUser(ChatUser user) {
        if (repository.has(user)) {
            log.warn("user with name {} already exists", user.getName());
            throw new UserAlreadyExistsException(user);
        }
        log.debug("saving new user: {}", user);
        user = repository.save(user);
        log.debug("successfully saved user: {}", user);
        return user;
    }

    public ChatUser getUser(String sessionId, int id) {
        checkSession(sessionId);
        if (!repository.hasUser(id)) {
            log.warn("user not found. id: {}", id);
            throw new UserNotFoundException(id);
        }
        return repository.getUser(id);
    }

    public ChatSession login(ChatUser user) {
        log.debug("login user: {}", user.getName());
        if (!repository.login(user)) {
            log.warn("login failed. Invalid username or password");
            throw new LoginFailedException();
        }
        log.debug("login successful. creating session for user: {}", user.getName());
        user = repository.getUser(user.getName());
        String sessionId = UUID.randomUUID().toString();

        TimeUnit sessionTimeLiveUnit = TimeUnit.valueOf(SERVICE_CONFIG.getProperty("session_time_units"));
        int sessionTimeLive = Integer.parseInt(SERVICE_CONFIG.getProperty("session_time_live"));
        ChatSession session = ServiceMapper.toSession(sessionId, user, sessionTimeLive, sessionTimeLiveUnit);
        repository.save(session);
        log.debug("successfully created session: {}", session);
        return session;
    }

    public Chat createChat(String sessionId, Chat chat) {
        checkSession(sessionId);
        log.debug("creating chat. sessionId: {} chat: {}", sessionId, chat);
        chat = repository.save(chat);
        log.debug("{}: successfully saved chat: {}", sessionId, chat);
        return chat;
    }

    public ChatMessage createMessage(String sessionId, ChatMessage message) {
        checkSession(sessionId);
        log.debug("creating message. sessionId: {}, message: {}", sessionId, message);
        message = repository.save(message);
        log.debug("successfully saved message: {}. sessionId: {}", message, sessionId);
        return message;
    }

    public void activateSession(String sessionId) {
        log.debug("activating session with id: {}", sessionId);
        ChatSession session = repository.getSession(sessionId);

        TimeUnit sessionTimeLiveUnit = TimeUnit.valueOf(SERVICE_CONFIG.getProperty("session_time_units"));
        int sessionTimeLive = Integer.parseInt(SERVICE_CONFIG.getProperty("session_time_live"));
        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(sessionTimeLiveUnit.toMinutes(sessionTimeLive));

        log.debug("updating session expire date. old value: {}; new value: {}", session.getExpiredDate(), expiredDate);
        session.setExpiredDate(expiredDate);
        repository.updateSessionExpiredDate(session);
        log.debug("successfully updated session: {}", session);
    }

    public ChatSession getSession(String sessionId) {
        log.debug("getting session. sessionId: {}", sessionId);
        return repository.getSession(sessionId);
    }

    public List<ChatSession> getActiveSessions() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("getting active sessions. current timestamp: {}", now);
        return repository.getSessionsAfter(now);
    }

    public List<ChatInfo> getUserChatsInfo(String sessionId, ChatUser user) {
        checkSession(sessionId);
        log.debug("getting chats for user: {}", user);
        return repository.getUserChatsInfo(user);
    }

    public List<ChatMessage> getChatMessages(String sessionId, ChatInfo chat) {
        checkSession(sessionId);
        int messagesLimit = Integer.parseInt(SERVICE_CONFIG.getProperty("message_limit"));
        log.debug("getting last {} messages for chat: {}", messagesLimit, chat);
        return repository.getLastMessages(chat, messagesLimit);
    }

    public ChatInfo getChatInfo(String sessionId, int chatId) {
        checkSession(sessionId);
        log.debug("getting chat info for chat with id: {}", chatId);
        return repository.getChatInfo(chatId);
    }

    public List<ChatUser> getChatUsers(String sessionId, ChatInfo chat) {
        checkSession(sessionId);
        log.debug("getting users for chat: {}", chat);
        return repository.getChatUsers(chat);
    }
}
