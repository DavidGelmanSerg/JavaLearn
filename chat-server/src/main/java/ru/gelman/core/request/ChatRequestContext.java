package ru.gelman.core.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import ru.gelman.core.client.TcpClient;
import ru.gelman.core.client.TcpClientManager;

@Getter
public class ChatRequestContext {
    private final ChatRequest request;
    private final TcpClient client;
    private final TcpClientManager sessionManager;

    public ChatRequestContext(ChatRequest request, TcpClient client, TcpClientManager sessionManager) throws JsonProcessingException {
        this.request = request;
        this.client = client;
        this.sessionManager = sessionManager;
    }

}
