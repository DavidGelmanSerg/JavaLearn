package ru.gelman.core.response.sender;

import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.network.data.NetMessage;

public class InitiatorSender implements ChatResponseSender {
    @Override
    public void send(ChatRequestContext context, NetMessage response) {
        context.getClient().sendMessage(response);
    }
}
