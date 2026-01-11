package ru.gelman.entity.chat;

import lombok.Data;
import ru.gelman.entity.ChatUser;
import ru.gelman.entity.message.ChatMessage;

import java.util.List;

@Data
public class Chat {
    private final ChatInfo info;
    private final List<ChatUser> users;
    private final List<ChatMessage> messages;

    public static Chat existing(int id, String name, int creatorId, boolean deleted, List<ChatUser> users, List<ChatMessage> messages) {
        ChatInfo info = ChatInfo.ofExistingChat(id, creatorId, name, deleted);
        return new Chat(info, users, messages);
    }

    public static Chat createNew(String name, int creatorId, List<ChatUser> users) {
        ChatInfo info = ChatInfo.ofNewChat(name, creatorId);
        return new Chat(info, users, List.of());
    }
}
