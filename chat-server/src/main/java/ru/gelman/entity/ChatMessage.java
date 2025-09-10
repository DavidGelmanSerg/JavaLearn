package ru.gelman.entity;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatMessage implements Comparable<ChatMessage> {
    @ToString.Include
    private final int chatId;
    @ToString.Include
    private final int creatorId;
    @NonNull
    private final LocalDateTime creationDateTime;
    @Setter
    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer id;
    @Setter
    @NonNull
    private String content;
    @Setter
    @ToString.Include
    private boolean deleted;

    private ChatMessage(Integer id, int chatId, int creatorId, String content, LocalDateTime creationDateTime, boolean deleted) {
        this.id = id;
        this.chatId = chatId;
        this.creatorId = creatorId;

        if (content.isEmpty()) {
            throw new IllegalArgumentException("Empty content");
        }
        this.content = content;
        this.creationDateTime = creationDateTime;
        this.deleted = deleted;
    }

    public static ChatMessage newMessage(int chatId, int creatorId, String content, LocalDateTime creationDateTime) {
        return new ChatMessage(null, chatId, creatorId, content, creationDateTime, false);
    }

    public static ChatMessage existingMessage(int id, int chatId, int creatorId, String content, LocalDateTime creationDateTime) {
        return new ChatMessage(id, chatId, creatorId, content, creationDateTime, false);
    }

    @Override
    public int compareTo(ChatMessage o) {
        return creationDateTime.compareTo(o.creationDateTime);
    }
}
