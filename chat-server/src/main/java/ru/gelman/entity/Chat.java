package ru.gelman.entity;

import lombok.*;

import java.util.List;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
public class Chat implements Comparable<Chat> {
    @NonNull
    @ToString.Include
    private final ChatInfo info;
    @NonNull
    private final List<ChatUser> users;
    @NonNull
    private final List<ChatMessage> messages;

    public void setId(int id) {
        info.setId(id);
    }

    public void setName(String name) {
        info.setName(name);
    }

    public void setDeleted(boolean deleted) {
        info.setDeleted(deleted);
    }

    @Override
    public int compareTo(Chat o) {
        ChatMessage otherLastMessage = o.messages.get(o.messages.size() - 1);
        ChatMessage lastMessage = messages.get(messages.size() - 1);
        return lastMessage.compareTo(otherLastMessage);
    }
}
