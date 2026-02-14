package ru.gelman.entity;

import lombok.Data;
import lombok.ToString;

@Data
public class ChatUser {
    private Integer id;
    private String name;
    @ToString.Exclude
    private String password;
    private boolean deleted;
    private boolean isOnline;

    public static ChatUser existing(int id, String name, String password, boolean deleted, boolean isOnline) {
        ChatUser user = new ChatUser();
        user.setId(id);
        user.setName(name);
        user.setPassword(password);
        user.setDeleted(deleted);
        user.setOnline(isOnline);
        return user;
    }

    public static ChatUser createNew(String name, String password) {
        ChatUser user = new ChatUser();
        user.setId(null);
        user.setName(name);
        user.setPassword(password);
        user.setDeleted(false);
        user.setOnline(false);
        return user;
    }
}
