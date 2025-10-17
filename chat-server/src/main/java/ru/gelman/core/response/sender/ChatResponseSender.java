package ru.gelman.core.response.sender;

import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.response.ChatResponse;

public interface ChatResponseSender {
    void send(ChatRequestContext context, ChatResponse response);
}
