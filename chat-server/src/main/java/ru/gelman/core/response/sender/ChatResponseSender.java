package ru.gelman.core.response.sender;

import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.network.data.NetMessage;

public interface ChatResponseSender {
    void send(ChatRequestContext context, NetMessage response);
}
