package ru.gelman.core.request.handler.user;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.controller.ChatController;
import ru.gelman.core.request.ChatRequest;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.ChatResponse;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.InitiatorSender;
import ru.gelman.dto.UserData;
import ru.gelman.service.exception.ChatServiceException;

@Slf4j
public class CreateUserHandler implements ChatRequestHandler {
    private final ChatController controller;
    private final ChatResponseSender sender;

    public CreateUserHandler(ChatController controller) {
        this.controller = controller;
        this.sender = new InitiatorSender();
    }

    @Override
    public void handle(ChatRequestContext context) {
        ChatRequest request = context.getRequest();
        ChatResponse response;
        String name = request.getBodyValue("name");
        String password = request.getBodyValue("password");
        UserData createdUser = controller.createUser(name, password);
        response = ChatResponse.success(request.getHeaders());
        response.setBodyValue("user", createdUser);
        sender.send(context, response);
    }
}
