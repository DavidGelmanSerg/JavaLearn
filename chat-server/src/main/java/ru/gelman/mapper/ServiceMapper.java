package ru.gelman.mapper;

import ru.gelman.dto.*;
import ru.gelman.entity.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServiceMapper {

    public static UserData toUserDto(ChatUser user) {
        return new UserData(user.getId(), user.getName());
    }

    public static SessionData toSessionDto(ChatSession session) {
        String expiredDate = session.getExpiredDate().format(DateTimeFormatter.ISO_DATE_TIME);
        return new SessionData(toUserDto(session.getUser()), session.getSessionId(), expiredDate);
    }

    public static ChatData toChatDto(Chat chat) {
        ChatInfoData chatInfo = toChatInfoDto(chat.getInfo());
        List<UserData> users = chat.getUsers().stream().map(ServiceMapper::toUserDto).toList();
        List<MessageData> messages = chat.getMessages().stream().map(ServiceMapper::toMessageDto).toList();
        return new ChatData(chatInfo, users, messages);
    }

    public static ChatInfoData toChatInfoDto(ChatInfo info) {
        int id = info.getId();
        String name = info.getName();
        int creatorId = info.getCreatorId();
        boolean deleted = info.isDeleted();
        return new ChatInfoData(id, name, creatorId, deleted);
    }

    public static MessageData toMessageDto(ChatMessage message) {
        String messageDateTime = message.getCreationDateTime().format(DateTimeFormatter.ISO_DATE_TIME);
        return new MessageData(message.getId(), message.getChatId(), toUserDto(message.getCreator()), message.getContent(), messageDateTime);
    }

    public static ChatUser toUser(String name, String password) {
        return new ChatUser(null, name, password, false);
    }

    public static ChatUser toUser(Integer id, String name, String password) {
        return new ChatUser(id, name, password, false);
    }

    public static Chat toChat(String name, int creatorId, List<ChatUser> users) {
        ChatInfo info = new ChatInfo(creatorId, null, name, false);
        return new Chat(info, users, List.of());
    }

    public static Chat toChat(ChatInfo info, List<ChatMessage> messages, List<ChatUser> users) {
        return new Chat(info, users, messages);
    }

    public static ChatMessage toMessage(int chatId, ChatUser creator, String content, String creationDateTime) {
        LocalDateTime messageDateTime = LocalDateTime.parse(creationDateTime, DateTimeFormatter.ISO_DATE_TIME);
        return new ChatMessage(chatId, creator, messageDateTime, null, content, false);
    }

    public static ChatMessage toMessage(int id, int chatId, ChatUser creator, String content, LocalDateTime creationDateTime, boolean deleted) {
        return new ChatMessage(chatId, creator, creationDateTime, id, content, deleted);
    }

    public static ChatSession toSession(String sessionId, ChatUser user, int sessionTimeLive, TimeUnit unit) {
        LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(unit.toMinutes(sessionTimeLive));
        return new ChatSession(sessionId, user, expiredDate);
    }

    public static ChatSession toSession(String sessionId, ChatUser user, LocalDateTime expired) {
        return new ChatSession(sessionId, user, expired);
    }

    public static ChatInfo toChatInfo(Integer id, String name, Integer creatorId, boolean deleted) {
        return new ChatInfo(creatorId, id, name, deleted);
    }
}
