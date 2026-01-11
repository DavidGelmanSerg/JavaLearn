package ru.gelman.core.request.handler.chat;

import ru.gelman.core.ChatAction;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.ChatSender;
import ru.gelman.dto.CreateMessageRq;
import ru.gelman.dto.MessageData;
import ru.gelman.dto.ResponseStatus;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;

public class AddMessageHandler implements ChatRequestHandler {
    private final ChatResponseSender sender;

    public AddMessageHandler() {
        this.sender = new ChatSender();
    }

    @Override
    public void handle(ChatRequestContext context, NetMessage request) {
        String sessionId = request.getHeader("sessionId");
        CreateMessageRq messageRq = request.getBodyValue("message", CreateMessageRq.class);
        MessageData added = context.getChatController().createMessage(sessionId, messageRq);

        NetMessage response = new JsonNetMessage();
        response.setHeaders(request.getHeaders());
        response.setBodyValue("message", added);
        response.setBodyValue("status", ResponseStatus.success());
        sender.send(context, response);
    }

    @Override
    public boolean canHandle(NetMessage request) {
        String value = request.getHeader("action").toUpperCase();
        return ChatAction.valueOf(value) == ChatAction.MESSAGE_CREATE;
    }
}
