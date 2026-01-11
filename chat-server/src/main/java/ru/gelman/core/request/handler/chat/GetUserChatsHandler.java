package ru.gelman.core.request.handler.chat;

import ru.gelman.core.ChatAction;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.InitiatorSender;
import ru.gelman.dto.ChatInfoData;
import ru.gelman.dto.ResponseStatus;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;

import java.util.List;

public class GetUserChatsHandler implements ChatRequestHandler {
    private final ChatResponseSender sender;

    public GetUserChatsHandler() {
        this.sender = new InitiatorSender();
    }

    @Override
    public void handle(ChatRequestContext context, NetMessage request) {
        String sessionId = request.getHeader("sessionId");
        int id = Integer.parseInt(request.getBodyValue("id"));
        List<ChatInfoData> userChats = context.getChatController().getUserChatInfos(sessionId, id);
        
        NetMessage response = new JsonNetMessage();
        response.setHeaders(request.getHeaders());
        response.setListToBody("chats", userChats);
        response.setBodyValue("status", ResponseStatus.success());
        sender.send(context, response);
    }

    @Override
    public boolean canHandle(NetMessage request) {
        String value = request.getHeader("action").toUpperCase();
        return ChatAction.valueOf(value) == ChatAction.USER_GET;
    }
}
