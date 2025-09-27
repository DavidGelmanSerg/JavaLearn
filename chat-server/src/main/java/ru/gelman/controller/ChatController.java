package ru.gelman.controller;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.dto.*;
import ru.gelman.entity.*;
import ru.gelman.mapper.ServiceMapper;
import ru.gelman.service.ChatService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ChatController {
    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    public UserData createUser(String name, String password) {
        log.info("creating user with name: {}", name);
        ChatUser created = service.createUser(ServiceMapper.toUser(name, password));
        return ServiceMapper.toUserDto(created);
    }

    public SessionData login(String name, String password) {
        log.info("login user with name: {}", name);
        ChatUser user = ServiceMapper.toUser(name, password);
        ChatSession session = service.login(user);
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
        Chat chat = service.createChat(sessionId, ServiceMapper.toChat(rq.name(), rq.creatorId(), users));
        return ServiceMapper.toChatDto(chat);
    }

    public MessageData createMessage(String sessionId, CreateMessageRq rq) {
        log.info("{}: creating message. chatId: {}, content: {}", sessionId, rq.chatId(), rq.content());
        ChatUser creator = service.getUser(sessionId, rq.creatorId());
        ChatMessage message = service.createMessage(sessionId, ServiceMapper.toMessage(rq.chatId(), creator, rq.content(), rq.creationDateTime()));
        return ServiceMapper.toMessageDto(message);
    }

    public SessionData activateSession(String sessionId) {
        log.info("activating session. sessionId: {}", sessionId);
        service.activateSession(sessionId);
        ChatSession session = service.getSession(sessionId);
        return ServiceMapper.toSessionDto(session);
    }

    public List<SessionData> getActiveSessions() {
        log.info("getting all active sessions");
        List<ChatSession> sessions = service.getActiveSessions();
        return sessions.stream().map(ServiceMapper::toSessionDto).collect(Collectors.toList());
    }

    public List<ChatInfoData> getUserChatInfos(String sessionId, int id) {
        log.info("getting chat infos for user with id: {}", id);
        ChatUser user = service.getUser(sessionId, id);
        List<ChatInfo> chatInfos = service.getUserChatsInfo(sessionId, user);
        return chatInfos.stream().map(ServiceMapper::toChatInfoDto).collect(Collectors.toList());
    }

    public List<MessageData> getChatMessages(String sessionId, int chatId) {
        log.info("getting chat messages for chat with id: {}", chatId);
        ChatInfo chat = service.getChatInfo(sessionId, chatId);
        List<ChatMessage> messages = service.getChatMessages(sessionId, chat);
        return messages.stream().map(ServiceMapper::toMessageDto).collect(Collectors.toList());
    }

    public List<UserData> getChatUsers(String sessionId, int chatId) {
        log.info("getting chat users for chat with id: {}", chatId);
        ChatInfo chat = service.getChatInfo(sessionId, chatId);
        List<ChatUser> users = service.getChatUsers(sessionId, chat);
        return users.stream().map(ServiceMapper::toUserDto).collect(Collectors.toList());
    }
}
