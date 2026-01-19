package ru.gelman.repository;

import ru.gelman.entity.ChatUser;

public interface UserRepository {
    boolean has(ChatUser user);

    boolean has(int id);

    boolean has(String name);

    ChatUser save(ChatUser user);

    ChatUser getUser(String name);

    ChatUser getUser(int id);

}
