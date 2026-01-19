package ru.gelman.core.request;

import lombok.Builder;
import lombok.Getter;
import ru.gelman.controller.ChatController;
import ru.gelman.controller.SessionController;
import ru.gelman.controller.UserController;
import ru.gelman.core.ClientManager;
import ru.gelman.network.client.Client;

@Getter
@Builder
public class ChatRequestContext {
    private final Client client;
    private final ClientManager sessionManager;
    private final ChatController chatController;
    private final SessionController sessionController;
    private final UserController userController;
}
