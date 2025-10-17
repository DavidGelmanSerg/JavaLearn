package ru.gelman.core.request.handler.chat;

import ru.gelman.controller.ChatController;
import ru.gelman.core.request.ChatRequest;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.ChatResponse;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.ChatSender;
import ru.gelman.dto.ChatData;
import ru.gelman.dto.CreateChatRq;

import java.util.List;

public class CreateChatHandler implements ChatRequestHandler {
    private final ChatController controller;
    private final ChatResponseSender sender;

    public CreateChatHandler(ChatController controller) {
        this.controller = controller;
        this.sender = new ChatSender(controller);
    }

    @Override
    public void handle(ChatRequestContext context) {
        ChatRequest request = context.getRequest();
        String sessionId = request.getHeader("sessionId");
        String name = request.getBodyValue("name");
        int creatorId = Integer.parseInt(request.getBodyValue("creatorId"));
        List<Integer> userIds = request.getListFromBody("users", Integer.class);
        CreateChatRq rq = new CreateChatRq(name, creatorId, userIds);
        ChatData createdChat = controller.createChat(sessionId, rq);

        ChatResponse response = ChatResponse.success(request.getHeaders());
        response.setBodyValue("chat", createdChat);
        sender.send(context, response);
    }
}
