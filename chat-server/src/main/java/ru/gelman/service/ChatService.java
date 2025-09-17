package ru.gelman.service;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.PropertyLoader;
import ru.gelman.entity.*;
import ru.gelman.repository.ChatRepository;
import ru.gelman.service.exception.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ChatService {
    private static final Properties SERVICE_CONFIG = PropertyLoader.load("service_config.properties");
    private final ChatRepository repository;

    public ChatService(ChatRepository repository) {
        this.repository = repository;
    }

    private void checkSession(String sessionId) {
        log.debug("checking session: {}", sessionId);
        ChatSession session = repository.getSession(sessionId);
        if (session == null) {
            log.debug("session not found. id: {}", sessionId);
            throw new SessionNotFoundException(sessionId);
        }
        if (LocalDateTime.now().isAfter(session.getExpiredDate())) {
            log.debug("session is inactive. id: {}", sessionId);
            throw new SessionInactiveException(sessionId);
        }
        log.debug("session {} exists and is active", session);
    }

    public ChatUser createUser(String name, String password) {
        ChatUser user = ChatEntityFactory.newUser(name, password);
        if (repository.has(user)) {
            log.warn("user with name {} already exists", name);
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

    public ChatSession login(String name, String password) {
        log.debug("login user: {}", name);
        if (!repository.login(name, password)) {
            log.warn("login failed. Invalid username or password");
            throw new LoginFailedException();
        }
        log.debug("login successful. creating session for user: {}", name);
        ChatUser user = repository.getUser(name);
        String sessionId = UUID.randomUUID().toString();

        TimeUnit sessionTimeLiveUnit = TimeUnit.valueOf(SERVICE_CONFIG.getProperty("session_time_units"));
        int sessionTimeLive = Integer.parseInt(SERVICE_CONFIG.getProperty("session_time_live"));
        ChatSession session = ChatEntityFactory.newSession(sessionId, user, sessionTimeLive, sessionTimeLiveUnit);
        repository.save(session);
        log.debug("successfully created session: {}", session);
        return session;
    }

    public Chat createChat(String sessionId, String name, int creatorId, List<ChatUser> users) {
        checkSession(sessionId);
        log.debug("{}: creating chat. name: {}; creating by: {} users: {}", sessionId, name, creatorId, users);
        Chat chat = repository.save(ChatEntityFactory.newChat(name, creatorId, users));
        log.debug("{}: successfully saved chat: {}", sessionId, chat);
        return chat;
    }

    public ChatMessage createMessage(String sessionId, int chatId, int creatorId, String content, String creationDateTime) {
        checkSession(sessionId);
        log.debug("{} creating message. chatId: {}, creatorId {}, content: {}, timestamp: {}", sessionId, chatId, creatorId, content, creationDateTime);
        ChatMessage message = repository.save(ChatEntityFactory.newMessage(chatId, creatorId, content, creationDateTime));
        log.debug("{}: successfully saved message: {}", sessionId, message);
        return message;
    }
}
