package ru.gelman.controller;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.dto.SessionData;
import ru.gelman.dto.UserData;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;
import ru.gelman.mapper.ServiceMapper;
import ru.gelman.service.ChatService;

@Slf4j
public class ChatController {
    private final ChatService service;
    private final ServiceMapper mapper;

    public ChatController(ChatService service) {
        this.service = service;
        this.mapper = new ServiceMapper();
    }

    public UserData createUser(String name, String password) {
        log.info("creating user with name: {}", name);
        ChatUser created = service.createUser(name, password);
        return mapper.toUserDto(created);
    }

    public SessionData login(String name, String password) {
        log.info("login user with name: {}", name);
        ChatSession session = service.login(name, password);
        return mapper.toSessionDto(session);
    }

    public UserData getUser(String sessionId, int id) {
        log.info("getting user by id: {}, sessionId: {}", id, sessionId);
        ChatUser user = service.getUser(sessionId, id);
        return mapper.toUserDto(user);
    }
}
