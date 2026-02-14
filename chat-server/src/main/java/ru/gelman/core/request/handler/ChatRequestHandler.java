package ru.gelman.core.request.handler;

import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.network.data.NetMessage;

public interface ChatRequestHandler {
    void handle(ChatRequestContext context, NetMessage request);

    boolean canHandle(NetMessage request);
}
