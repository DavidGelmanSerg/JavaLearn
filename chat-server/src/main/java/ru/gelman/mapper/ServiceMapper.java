package ru.gelman.mapper;

import ru.gelman.dto.*;
import ru.gelman.entity.Chat;
import ru.gelman.entity.ChatMessage;
import ru.gelman.entity.ChatSession;
import ru.gelman.entity.ChatUser;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServiceMapper {

    public static UserData toUserDto(ChatUser user) {
        return new UserData(user.getId(), user.getName());
    }

    public static SessionData toSessionDto(ChatSession session) {
        String expiredDate = session.getExpiredDate().format(DateTimeFormatter.ISO_DATE_TIME);
        return new SessionData(toUserDto(session.getUser()), session.getSessionId(), expiredDate);
    }

    public static ChatData toChatDto(Chat chat) {
        ChatInfoData chatInfo = toChatInfoDto(chat);
        List<UserData> users = chat.getUsers().stream().map(ServiceMapper::toUserDto).toList();
        List<MessageData> messages = chat.getMessages().stream().map(ServiceMapper::toMessageDto).toList();
        return new ChatData(chatInfo, users, messages);
    }

    public static ChatInfoData toChatInfoDto(Chat chat) {
        int id = chat.getInfo().getId();
        String name = chat.getInfo().getName();
        int creatorId = chat.getInfo().getCreatorId();
        boolean deleted = chat.getInfo().isDeleted();
        return new ChatInfoData(id, name, creatorId, deleted);
    }

    public static MessageData toMessageDto(ChatMessage message) {
        String messageDateTime = message.getCreationDateTime().format(DateTimeFormatter.ISO_DATE_TIME);
        return new MessageData(message.getId(), message.getChatId(), message.getCreatorId(), message.getContent(), messageDateTime);
    }
}
