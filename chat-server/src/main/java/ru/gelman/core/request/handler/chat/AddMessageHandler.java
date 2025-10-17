package ru.gelman.core.request.handler.chat;

import ru.gelman.controller.ChatController;
import ru.gelman.core.request.ChatRequest;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.ChatResponse;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.ChatSender;
import ru.gelman.dto.CreateMessageRq;
import ru.gelman.dto.MessageData;

public class AddMessageHandler implements ChatRequestHandler {
    private final ChatController controller;
    private final ChatResponseSender sender;

    public AddMessageHandler(ChatController controller) {
        this.controller = controller;
        this.sender = new ChatSender(controller);
    }

    @Override
    public void handle(ChatRequestContext context) {
        ChatRequest request = context.getRequest();
        String sessionId = request.getHeader("sessionId");
        CreateMessageRq messageRq = request.getBodyValueAsObject("message", CreateMessageRq.class);
        MessageData added = controller.createMessage(sessionId, messageRq);

        ChatResponse response = ChatResponse.success(request.getHeaders());
        response.setBodyValue("message", added);
        sender.send(context, response);
    }
}
