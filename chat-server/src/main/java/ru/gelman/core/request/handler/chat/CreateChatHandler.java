package ru.gelman.core.request.handler.chat;

import ru.gelman.core.ChatAction;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.ChatSender;
import ru.gelman.dto.ChatData;
import ru.gelman.dto.CreateChatRq;
import ru.gelman.dto.ResponseStatus;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;

import java.util.List;

public class CreateChatHandler implements ChatRequestHandler {
    private final ChatResponseSender sender;

    public CreateChatHandler() {
        this.sender = new ChatSender();
    }

    @Override
    public void handle(ChatRequestContext context, NetMessage request) {
        String sessionId = request.getHeader("sessionId");
        String name = request.getBodyValue("name");
        int creatorId = Integer.parseInt(request.getBodyValue("creatorId"));
        List<Integer> userIds = request.getListFromBody("users", Integer.class);
        CreateChatRq rq = new CreateChatRq(name, creatorId, userIds);
        ChatData createdChat = context.getChatController().createChat(sessionId, rq);

        NetMessage response = new JsonNetMessage();
        response.setHeaders(request.getHeaders());
        response.setBodyValue("chat", createdChat);
        response.setBodyValue("status", ResponseStatus.success());
        sender.send(context, response);
    }

    @Override
    public boolean canHandle(NetMessage request) {
        String value = request.getHeader("action").toUpperCase();
        return ChatAction.valueOf(value) == ChatAction.CHAT_CREATE;
    }
}
