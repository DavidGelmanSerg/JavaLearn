package ru.gelman.core.request;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.core.ChatAction;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.response.sender.ChatResponseSender;
import ru.gelman.core.response.sender.InitiatorSender;
import ru.gelman.dto.ResponseStatus;
import ru.gelman.network.client.Client;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;
import ru.gelman.service.exception.ChatServiceException;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ChatServerRequestHandler implements ChatRequestHandler, Runnable {
    private final List<ChatRequestHandler> handlers;
    private final ChatResponseSender sender;
    private final ChatRequestContext context;

    public ChatServerRequestHandler(List<ChatRequestHandler> handlers, ChatRequestContext context) {
        this.handlers = handlers;
        this.context = context;
        this.sender = new InitiatorSender();
    }

    @Override
    public void handle(ChatRequestContext context, NetMessage request) {
        try {
            ChatAction action = ChatAction.valueOf(request.getHeader("action").toUpperCase());
            log.info("got {} action request", action);
            ChatRequestHandler handler = handlers.stream()
                    .filter(h -> h.canHandle(request))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("could not find handler for action " + action));

            log.info("starting {} handler for action {}", handler.getClass().getName(), action);
            handler.handle(context, request);
        } catch (ChatServiceException e) {
            log.warn("got service error. message: {}, type: {}", e.getMessage(), e.getErrorType());
            NetMessage response = new JsonNetMessage();
            response.setHeaders(request.getHeaders());
            response.setBodyValue("status", ResponseStatus.error(e.getMessage()));
            sender.send(context, response);
        } catch (RuntimeException e) {
            log.warn("got err or while handling request. message: {}", e.getMessage());
            NetMessage response = new JsonNetMessage();
            response.setHeaders(request.getHeaders());
            response.setBodyValue("status", ResponseStatus.error(e.getMessage()));
            sender.send(context, response);
        }
    }

    @Override
    public boolean canHandle(NetMessage request) {
        return true;
    }

    @Override
    public void run() {
        try (Client client = context.getClient()) {
            while (!client.isClosed()) {
                NetMessage request = client.getMessage();
                log.debug("accept request: {}", request.asStringValue());
                handle(context, request);
            }
        } catch (IOException e) {
            log.error("IOException {} occurred", e.getMessage());
        }
    }
}
