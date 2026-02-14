package ru.gelman.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.dto.SessionData;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;
import ru.gelman.entity.chat.ChatInfo;
import ru.gelman.mapper.SessionMapper;
import ru.gelman.service.ChatService;
import ru.gelman.service.SessionService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class SessionController {
    private final SessionService sessionService;
    private final ChatService chatService;
    private final SessionMapper sessionMapper;

    public SessionData activateSession(String sessionId) {
        log.info("activating session. sessionId: {}", sessionId);
        sessionService.activateSession(sessionId);
        ChatSession session = sessionService.getSession(sessionId);
        return sessionMapper.toSessionDto(session);
    }

    public List<SessionData> getActiveSessionsForChat(String sessionId, int id) {
        log.info("getting active sessions for chat with id: {}", id);
        sessionService.checkSession(sessionId);
        ChatInfo chat = chatService.getChatInfo(id);
        List<ChatUser> users = chatService.getChatUsers(chat);
        List<ChatSession> activeSessions = sessionService.getActiveSessions();
        return activeSessions.stream().filter(session -> users.contains(session.getUser())).map(sessionMapper::toSessionDto).collect(Collectors.toList());
    }

    public SessionData login(String name, String password) {
        log.info("login user with name: {}", name);
        ChatUser user = ChatUser.createNew(name, password);
        ChatSession session = sessionService.login(user);
        return sessionMapper.toSessionDto(session);
    }
}
