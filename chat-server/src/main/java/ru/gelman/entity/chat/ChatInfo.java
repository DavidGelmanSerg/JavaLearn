package ru.gelman.entity.chat;

import lombok.Data;

@Data
public class ChatInfo {
    private final int creatorId;
    private Integer id;
    private String name;
    private boolean deleted;

    public static ChatInfo ofExistingChat(Integer id, int creatorId, String name, boolean deleted) {
        ChatInfo info = new ChatInfo(creatorId);
        info.setId(id);
        info.setName(name);
        info.setDeleted(deleted);
        return info;
    }

    public static ChatInfo ofNewChat(String name, int creatorId) {
        ChatInfo info = new ChatInfo(creatorId);
        info.setId(null);
        info.setName(name);
        info.setDeleted(false);
        return info;
    }
}
