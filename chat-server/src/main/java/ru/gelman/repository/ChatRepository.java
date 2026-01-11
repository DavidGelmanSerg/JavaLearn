package ru.gelman.repository;


import ru.gelman.entity.chat.ChatInfo;
import ru.gelman.entity.ChatUser;
import ru.gelman.entity.chat.Chat;
import ru.gelman.entity.message.ChatMessage;

import java.util.List;

public interface ChatRepository {

    Chat save(Chat chat);

    List<ChatInfo> getUserChatsInfo(ChatUser user);

    ChatInfo getChatInfo(int chatId);

    List<ChatUser> getChatUsers(int chatId);

    ChatMessage save(ChatMessage message);

    List<ChatMessage> getLastMessages(int chatId, int messagesLimit);
}
