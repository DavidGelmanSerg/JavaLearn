package ru.gelman.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatUser {
    @ToString.Include
    @EqualsAndHashCode.Include
    private final Integer id;
    @Setter
    @ToString.Include
    private String name;
    @Setter
    private String password;
    @Setter
    @ToString.Include
    private boolean deleted;

    private ChatUser(Integer id, String name, String password, boolean deleted) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.deleted = deleted;
    }

    public static ChatUser newUser(String name, String password) {
        return new ChatUser(null, name, password, false);
    }

    public static ChatUser existingUser(Integer id, String name, String password) {
        return new ChatUser(id, name, password, false);
    }
}
