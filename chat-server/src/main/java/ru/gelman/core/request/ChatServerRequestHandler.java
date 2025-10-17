package ru.gelman.core.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.core.ChatAction;
import ru.gelman.core.client.TcpClient;
import ru.gelman.core.client.TcpClientManager;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.ChatResponse;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.InitiatorSender;
import ru.gelman.service.exception.ChatServiceException;

import java.net.Socket;
import java.util.Map;

@Slf4j
public class ChatServerRequestHandler implements Runnable, ChatRequestHandler {
    private final Map<ChatAction, ChatRequestHandler> handlers;
    private final TcpClientManager manager;
    private final Socket clientSocket;
    private final ChatResponseSender sender;

    public ChatServerRequestHandler(Map<ChatAction, ChatRequestHandler> handlers, TcpClientManager manager, Socket clientSocket) {
        this.handlers = handlers;
        this.manager = manager;
        this.clientSocket = clientSocket;
        this.sender = new InitiatorSender();
    }

    @Override
    public void run() {
        try {
            log.info("started general client handler");
            TcpClient client = new TcpClient(clientSocket);
            ChatRequest request = client.get();

            log.info("creating request context");
            ChatRequestContext context = new ChatRequestContext(request, client, manager);
            handle(context);
        } catch (JsonProcessingException e) {
            log.warn("failed to parse json from string. reason: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(ChatRequestContext context) {
        ChatRequest request = context.getRequest();
        ChatResponse response;
        try {
            ChatAction action = ChatAction.valueOf(context.getRequest().getHeader("action").toUpperCase());
            log.info("got {} action request", action);

            ChatRequestHandler handler = handlers.getOrDefault(action, null);
            if (handler == null) {
                log.info("handler for {} action not found", action);
                response = ChatResponse.error(
                        context.getRequest().getHeaders(),
                        "OPERATION NOT SUPPORTED",
                        "could not find handler for action " + action
                );
                sender.send(context, response);
            } else {
                log.info("starting {} handler for action {}", handler.getClass().getName(), action);
                handler.handle(context);
                log.info("successful handled client request");
            }
        } catch (ChatServiceException e) {
            log.warn("got service error. message: {}, type: {}", e.getMessage(), e.getErrorType());
            response = ChatResponse.error(request.getHeaders(), e.getErrorType(), e.getMessage());
            sender.send(context, response);
        } catch (RuntimeException e) {
            log.warn("got error while handling request. message: {}", e.getMessage());
            response = ChatResponse.error(request.getHeaders(), "runtime error", e.getMessage());
            sender.send(context, response);
        }
    }
}
