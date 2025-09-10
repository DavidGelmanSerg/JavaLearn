package ru.gelman.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat implements Comparable<Chat> {
    @NonNull
    private final List<ChatMessage> messages;
    @NonNull
    private final List<ChatUser> users;
    @Getter
    private final int creatorId;
    @Setter
    @NonNull
    @ToString.Include
    private String name;
    @Setter
    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer id;
    @Setter
    @ToString.Include
    private boolean deleted;

    private Chat(Integer id, String name, int creatorId, List<ChatMessage> messages, List<ChatUser> users, boolean deleted) {
        this.id = id;
        this.name = name;
        this.creatorId = creatorId;
        this.messages = new ArrayList<>(messages);
        this.users = users;
        this.deleted = deleted;
        Collections.sort(messages);
    }

    public static Chat newChat(String name, int creatorId, List<ChatUser> users) {
        return new Chat(null, name, creatorId, List.of(), users, false);
    }

    public static Chat existingChat(Integer id, String name, int creatorId, List<ChatMessage> messages, List<ChatUser> users) {
        return new Chat(id, name, creatorId, messages, users, false);
    }

    @Override
    public int compareTo(Chat o) {
        ChatMessage otherLastMessage = o.messages.get(o.messages.size() - 1);
        ChatMessage lastMessage = messages.get(messages.size() - 1);
        return lastMessage.compareTo(otherLastMessage);
    }
}
