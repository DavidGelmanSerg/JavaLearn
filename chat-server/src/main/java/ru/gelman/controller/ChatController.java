package ru.gelman.controller;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.dto.*;
import ru.gelman.entity.Chat;
import ru.gelman.entity.ChatMessage;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;
import ru.gelman.mapper.ServiceMapper;
import ru.gelman.service.ChatService;

import java.util.List;

@Slf4j
public class ChatController {
    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    public UserData createUser(String name, String password) {
        log.info("creating user with name: {}", name);
        ChatUser created = service.createUser(name, password);
        return ServiceMapper.toUserDto(created);
    }

    public SessionData login(String name, String password) {
        log.info("login user with name: {}", name);
        ChatSession session = service.login(name, password);
        return ServiceMapper.toSessionDto(session);
    }

    public UserData getUser(String sessionId, int id) {
        log.info("{}: getting user by id: {}", sessionId, id);
        ChatUser user = service.getUser(sessionId, id);
        return ServiceMapper.toUserDto(user);
    }

    public ChatData createChat(String sessionId, CreateChatRq rq) {
        log.info("{}: creating chat. name: {}; creatorId: {}; users: {}", sessionId, rq.name(), rq.creatorId(), rq.userIds());
        List<ChatUser> users = rq.userIds().stream().map(id -> service.getUser(sessionId, id)).toList();
        Chat chat = service.createChat(sessionId, rq.name(), rq.creatorId(), users);
        return ServiceMapper.toChatDto(chat);
    }

    public MessageData createMessage(String sessionId, CreateMessageRq rq) {
        log.info("{}: creating message. chatId: {}, content: {}", sessionId, rq.chatId(), rq.content());
        ChatMessage message = service.createMessage(sessionId, rq.chatId(), rq.creatorId(), rq.content(), rq.creationDateTime());
        return ServiceMapper.toMessageDto(message);
    }
}
