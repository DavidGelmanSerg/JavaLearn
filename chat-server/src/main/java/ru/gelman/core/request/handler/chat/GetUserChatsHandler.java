package ru.gelman.core.request.handler.chat;

import ru.gelman.controller.ChatController;
import ru.gelman.core.request.ChatRequest;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.ChatResponse;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.InitiatorSender;
import ru.gelman.dto.ChatInfoData;

import java.util.List;

public class GetUserChatsHandler implements ChatRequestHandler {
    private final ChatController controller;
    private final ChatResponseSender sender;

    public GetUserChatsHandler(ChatController controller) {
        this.controller = controller;
        this.sender = new InitiatorSender();
    }

    @Override
    public void handle(ChatRequestContext context) {
        ChatRequest request = context.getRequest();
        String sessionId = request.getHeader("sessionId");
        int id = Integer.parseInt(request.getBodyValue("id"));
        List<ChatInfoData> userChats = controller.getUserChatInfos(sessionId, id);
        ChatResponse response = ChatResponse.success(request.getHeaders());
        response.setListToBody("chats", userChats);
        sender.send(context, response);
    }
}
