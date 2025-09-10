package ru.gelman.service;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.PropertyLoader;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;
import ru.gelman.repository.ChatRepository;
import ru.gelman.service.exception.*;

import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChatService {
    private static final Properties SERVICE_CONFIG = PropertyLoader.load("service_config.properties");
    private final Set<ChatSession> sessions;
    private final ChatRepository repository;

    public ChatService(ChatRepository repository) {
        this.sessions = ConcurrentHashMap.newKeySet();
        this.repository = repository;
    }

    private ChatSession getSession(String sessionId) {
        for (ChatSession session : sessions) {
            if (session.getSessionId().equals(sessionId)) {
                return session;
            }
        }
        return null;
    }

    private void checkSession(String sessionId) {
        log.debug("checking session: {}", sessionId);
        ChatSession session = getSession(sessionId);
        if (session == null) {
            log.debug("session not found. id: {}", sessionId);
            throw new SessionNotFoundException(sessionId);
        }
        if (!session.isActive()) {
            log.debug("session is inactive. id: {}", sessionId);
            throw new SessionInactiveException(sessionId);
        }
        log.debug("session {} exists and is active", session);
    }

    public ChatUser createUser(String name, String password) {
        ChatUser user = ChatUser.newUser(name, password);
        if (repository.has(user)) {
            log.warn("user with name {} already exists", name);
            throw new UserAlreadyExistsException(user);
        }
        log.debug("saving new user: {}", user);
        repository.save(user);
        user = repository.getUser(name);
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
        ChatSession session = new ChatSession(sessionId, user);
        sessions.add(session);
        log.debug("successfully created session: {}", session);
        return session;
    }
}
