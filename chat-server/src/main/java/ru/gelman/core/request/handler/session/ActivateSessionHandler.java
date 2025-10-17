package ru.gelman.core.request.handler.session;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.controller.ChatController;
import ru.gelman.core.request.ChatRequest;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.ChatResponse;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.InitiatorSender;
import ru.gelman.dto.SessionData;

@Slf4j
public class ActivateSessionHandler implements ChatRequestHandler {
    private final ChatController controller;
    private final ChatResponseSender sender;

    public ActivateSessionHandler(ChatController controller) {
        this.controller = controller;
        this.sender = new InitiatorSender();
    }

    @Override
    public void handle(ChatRequestContext context) {
        ChatRequest request = context.getRequest();
        ChatResponse response;
        String sessionId = request.getHeader("sessionId");
        SessionData session = controller.activateSession(sessionId);
        response = ChatResponse.success(request.getHeaders());
        response.setBodyValue("session", session);
        sender.send(context, response);
    }
}
