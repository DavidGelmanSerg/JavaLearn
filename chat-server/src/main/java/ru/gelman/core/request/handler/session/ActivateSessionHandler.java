package ru.gelman.core.request.handler.session;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.core.ChatAction;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.InitiatorSender;
import ru.gelman.dto.ResponseStatus;
import ru.gelman.dto.SessionData;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;

@Slf4j
public class ActivateSessionHandler implements ChatRequestHandler {
    private final ChatResponseSender sender;

    public ActivateSessionHandler() {
        this.sender = new InitiatorSender();
    }

    @Override
    public void handle(ChatRequestContext context, NetMessage request) {
        String sessionId = request.getHeader("sessionId");
        SessionData session = context.getSessionController().activateSession(sessionId);
        NetMessage response = new JsonNetMessage();
        response.setHeaders(request.getHeaders());
        response.setBodyValue("session", session);
        response.setBodyValue("status", ResponseStatus.success());
        sender.send(context, response);
    }

    @Override
    public boolean canHandle(NetMessage request) {
        String value = request.getHeader("action").toUpperCase();
        return ChatAction.valueOf(value) == ChatAction.SESSION_ACTIVATE;
    }
}
