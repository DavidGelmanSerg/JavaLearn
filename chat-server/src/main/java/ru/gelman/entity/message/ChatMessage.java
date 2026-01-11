package ru.gelman.entity.message;


import lombok.Data;
import ru.gelman.entity.ChatUser;

import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private final int chatId;
    private final ChatUser creator;
    private final LocalDateTime creationDateTime;
    private String content;
    private boolean deleted;
    private Integer id;

    public static ChatMessage existing(int chatId, ChatUser creator, LocalDateTime creationDateTime, String content, boolean deleted, int id) {
        ChatMessage message = new ChatMessage(chatId, creator, creationDateTime);
        message.setContent(content);
        message.setDeleted(deleted);
        message.setId(id);
        return message;
    }

    public static ChatMessage createNew(int chatId, ChatUser creator, LocalDateTime creationDateTime, String content) {
        ChatMessage message = new ChatMessage(chatId, creator, creationDateTime);
        message.setContent(content);
        message.setDeleted(false);
        message.setId(null);
        return message;
    }
}
