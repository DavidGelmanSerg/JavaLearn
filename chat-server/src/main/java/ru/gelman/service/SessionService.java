package ru.gelman.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;
import ru.gelman.repository.SessionRepository;
import ru.gelman.repository.UserRepository;
import ru.gelman.service.exception.LoginFailedException;
import ru.gelman.service.exception.SessionInactiveException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
public class SessionService {
    private final SessionRepository repository;
    private final UserRepository userRepository;
    private final Properties serviceConfig;

    public void checkSession(String sessionId) {
        log.debug("checking session: {}", sessionId);
        ChatSession session = repository.getSession(sessionId);
        if (LocalDateTime.now().isAfter(session.getExpiredDate())) {
            log.debug("session is inactive. id: {}", sessionId);
            throw new SessionInactiveException(sessionId);
        }
        log.debug("session {} exists and is active", session);
    }

    public ChatSession createSession(ChatUser user) {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime expiredDate = getNewExpiredDate();
        ChatSession session = ChatSession.from(sessionId, user, expiredDate);
        repository.save(session);
        return session;
    }

    public void activateSession(String sessionId) {
        log.debug("activating session with id: {}", sessionId);
        ChatSession session = repository.getSession(sessionId);

        LocalDateTime expiredDate = getNewExpiredDate();
        log.debug("updating session expire date. old value: {}; new value: {}", session.getExpiredDate(), expiredDate);
        session.setExpiredDate(expiredDate);
        repository.updateSessionExpiredDate(session);
        log.debug("successfully updated session: {}", session);
    }

    public List<ChatSession> getActiveSessions() {
        LocalDateTime now = LocalDateTime.now();
        log.debug("getting active sessions. current timestamp: {}", now);
        return repository.getSessionsAfter(now);
    }

    public ChatSession getSession(String sessionId) {
        log.debug("getting session. sessionId: {}", sessionId);
        return repository.getSession(sessionId);
    }

    public ChatSession login(ChatUser user) {
        log.debug("login user: {}", user.getName());
        if (!repository.login(user)) {
            log.warn("login failed. Invalid username or password");
            throw new LoginFailedException();
        }
        log.debug("login successful. creating session for user: {}", user.getName());
        user = userRepository.getUser(user.getName());
        ChatSession session = createSession(user);
        log.debug("successfully created session: {}", session);
        return session;
    }

    private LocalDateTime getNewExpiredDate() {
        TimeUnit sessionTimeLiveUnit = TimeUnit.valueOf(serviceConfig.getProperty("session_time_units"));
        int sessionTimeLive = Integer.parseInt(serviceConfig.getProperty("session_time_live"));
        return LocalDateTime.now().plusMinutes(sessionTimeLiveUnit.toMinutes(sessionTimeLive));
    }
}
