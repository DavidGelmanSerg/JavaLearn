package ru.gelman.repository;


import ru.gelman.entity.Chat;
import ru.gelman.entity.ChatMessage;
import ru.gelman.entity.ChatUser;

public interface ChatRepository {

    boolean has(ChatUser user);

    boolean hasUser(int id);

    ChatUser save(ChatUser user);

    ChatUser getUser(String name);

    ChatUser getUser(int id);

    boolean login(String name, String password);

    boolean hasUser(String name);

    Chat save(Chat chat);

    ChatMessage save(ChatMessage message);
}
