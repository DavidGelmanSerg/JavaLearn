package ru.gelman.core.request.handler;

import ru.gelman.core.request.ChatRequestContext;

public interface ChatRequestHandler {
    void handle(ChatRequestContext context);
}
