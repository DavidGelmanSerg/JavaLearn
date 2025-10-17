package ru.gelman.core.response.sender;

import ru.gelman.controller.ChatController;
import ru.gelman.core.client.TcpClientManager;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.response.ChatResponse;
import ru.gelman.dto.SessionData;

public class ChatSender implements ChatResponseSender {
    private final ChatController controller;

    public ChatSender(ChatController controller) {
        this.controller = controller;
    }

    @Override
    public void send(ChatRequestContext context, ChatResponse response) {
        if (response.success()) {
            String sessionId = response.getHeader("sessionId");
            int chatId = Integer.parseInt(response.getBodyValue("id"));
            TcpClientManager manager = context.getSessionManager();
            for (SessionData session : controller.getActiveSessionsForChat(sessionId, chatId)) {
                manager.getClient(session.sessionId()).accept(response);
            }
        } else {
            context.getClient().accept(response);
        }
    }
}
