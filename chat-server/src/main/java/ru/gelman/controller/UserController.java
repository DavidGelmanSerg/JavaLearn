package ru.gelman.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.dto.UserData;
import ru.gelman.entity.ChatUser;
import ru.gelman.mapper.UserMapper;
import ru.gelman.service.SessionService;
import ru.gelman.service.UserService;

@AllArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;
    private final SessionService sessionService;
    private final UserMapper userMapper;

    public UserData createUser(String name, String password) {
        log.info("creating user with name: {}", name);
        ChatUser created = service.createUser(name, password);
        return userMapper.toUserDto(created);
    }

    public UserData getUser(String sessionId, int id) {
        sessionService.checkSession(sessionId);
        log.info("{}: getting user by id: {}", sessionId, id);
        ChatUser user = service.getUser(id);
        return userMapper.toUserDto(user);
    }
}
