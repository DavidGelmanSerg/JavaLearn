package ru.gelman.core.response.sender;

import com.fasterxml.jackson.databind.node.ObjectNode;
import ru.gelman.core.ClientManager;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.dto.SessionData;
import ru.gelman.network.client.Client;
import ru.gelman.network.data.NetMessage;

public class ChatSender implements ChatResponseSender {

    @Override
    public void send(ChatRequestContext context, NetMessage response) {
        String sessionId = response.getHeader("sessionId");
        int chatId = response.getBodyValue("chat", ObjectNode.class).get("info").get("id").asInt();
        ClientManager manager = context.getSessionManager();
        for (SessionData session : context.getSessionController().getActiveSessionsForChat(sessionId, chatId)) {
            Client client = manager.getClient(session.sessionId());
            client.sendMessage(response);
        }
    }
}
