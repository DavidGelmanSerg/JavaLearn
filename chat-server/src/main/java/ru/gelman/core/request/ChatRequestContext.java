package ru.gelman.core.request;

import lombok.Builder;
import lombok.Getter;
import ru.gelman.controller.ChatController;
import ru.gelman.controller.SessionController;
import ru.gelman.controller.UserController;
import ru.gelman.network.client.TcpClient;
import ru.gelman.core.ClientManager;

@Getter
@Builder
public class ChatRequestContext {
    private final TcpClient client;
    private final ClientManager sessionManager;
    private final ChatController chatController;
    private final SessionController sessionController;
    private final UserController userController;
}
