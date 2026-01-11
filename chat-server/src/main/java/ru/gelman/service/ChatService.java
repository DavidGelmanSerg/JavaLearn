package ru.gelman.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.PropertyLoader;
import ru.gelman.entity.ChatUser;
import ru.gelman.entity.chat.Chat;
import ru.gelman.entity.chat.ChatInfo;
import ru.gelman.entity.message.ChatMessage;
import ru.gelman.repository.ChatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Slf4j
@AllArgsConstructor
public class ChatService {
    private static final Properties SERVICE_CONFIG = PropertyLoader.load("/service_config.properties");
    private final ChatRepository chatRepository;

    public Chat createChat(String name, ChatUser creator, List<ChatUser> users) {
        Chat chat = Chat.createNew(name, creator.getId(), users);
        log.debug("creating chat: {}", chat);
        chat = chatRepository.save(chat);
        log.debug("successfully saved chat: {}", chat);
        return chat;
    }

    public ChatMessage createMessage(int chatId, ChatUser creator, String content, LocalDateTime creationDateTime) {
        ChatMessage message = ChatMessage.createNew(chatId, creator, creationDateTime, content);
        log.debug("creating  message: {}", message);
        message = chatRepository.save(message);
        log.debug("successfully saved message: {}", message);
        return message;
    }

    public List<ChatInfo> getUserChatsInfo(ChatUser user) {
        log.debug("getting chats for user: {}", user);
        return chatRepository.getUserChatsInfo(user);
    }

    public List<ChatMessage> getChatMessages(ChatInfo chat) {
        int messagesLimit = Integer.parseInt(SERVICE_CONFIG.getProperty("message_limit"));
        log.debug("getting last {} messages for chat: {}", messagesLimit, chat);
        return chatRepository.getLastMessages(chat.getId(), messagesLimit);
    }

    public ChatInfo getChatInfo(int chatId) {
        log.debug("getting chat info for chat with id: {}", chatId);
        return chatRepository.getChatInfo(chatId);
    }

    public List<ChatUser> getChatUsers(ChatInfo chat) {
        log.debug("getting users for chat: {}", chat);
        return chatRepository.getChatUsers(chat.getId());
    }
}
