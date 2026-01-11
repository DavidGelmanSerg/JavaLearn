package ru.gelman.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.dto.*;
import ru.gelman.entity.ChatUser;
import ru.gelman.entity.chat.Chat;
import ru.gelman.entity.chat.ChatInfo;
import ru.gelman.entity.message.ChatMessage;
import ru.gelman.mapper.ChatMapper;
import ru.gelman.mapper.MessageMapper;
import ru.gelman.mapper.UserMapper;
import ru.gelman.service.ChatService;
import ru.gelman.service.SessionService;
import ru.gelman.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;
    private final SessionService sessionService;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    public ChatData createChat(String sessionId, CreateChatRq rq) {
        sessionService.checkSession(sessionId);
        log.info("{}: creating chat. name: {}; creatorId: {}; users: {}", sessionId, rq.name(), rq.creatorId(), rq.userIds());
        List<ChatUser> users = rq.userIds().stream().map(userService::getUser).toList();
        ChatUser creator = userService.getUser(rq.creatorId());
        Chat chat = chatService.createChat(rq.name(), creator, users);
        return chatMapper.toChatDto(chat);
    }

    public MessageData createMessage(String sessionId, CreateMessageRq rq) {
        sessionService.checkSession(sessionId);
        log.info("{}: creating message. chatId: {}, content: {}", sessionId, rq.chatId(), rq.content());
        ChatUser creator = userService.getUser(rq.creatorId());
        LocalDateTime creationDateTime = LocalDateTime.parse(rq.creationDateTime(), DateTimeFormatter.ISO_DATE_TIME);
        ChatMessage message = chatService.createMessage(rq.chatId(), creator, rq.content(), creationDateTime);
        return messageMapper.toMessageDto(message);
    }


    public List<ChatInfoData> getUserChatInfos(String sessionId, int id) {
        sessionService.checkSession(sessionId);
        log.info("getting chat infos for user with id: {}", id);
        ChatUser user = userService.getUser(id);
        List<ChatInfo> chatInfos = chatService.getUserChatsInfo(user);
        return chatInfos.stream().map(chatMapper::toChatInfoDto).collect(Collectors.toList());
    }

    public List<MessageData> getChatMessages(String sessionId, int chatId) {
        sessionService.checkSession(sessionId);
        log.info("getting chat messages for chat with id: {}", chatId);
        ChatInfo chat = chatService.getChatInfo(chatId);
        List<ChatMessage> messages = chatService.getChatMessages(chat);
        return messages.stream().map(messageMapper::toMessageDto).collect(Collectors.toList());
    }

    public List<UserData> getChatUsers(String sessionId, int chatId) {
        sessionService.checkSession(sessionId);
        log.info("getting chat users for chat with id: {}", chatId);
        ChatInfo chat = chatService.getChatInfo(chatId);
        List<ChatUser> users = chatService.getChatUsers(chat);
        return users.stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }
}
