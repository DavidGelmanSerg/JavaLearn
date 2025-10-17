package ru.gelman;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.controller.ChatController;
import ru.gelman.core.ChatAction;
import ru.gelman.core.client.TcpClientManager;
import ru.gelman.core.request.ChatServerRequestHandler;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.core.request.handler.chat.AddMessageHandler;
import ru.gelman.core.request.handler.chat.CreateChatHandler;
import ru.gelman.core.request.handler.chat.GetUserChatsHandler;
import ru.gelman.core.request.handler.session.ActivateSessionHandler;
import ru.gelman.core.request.handler.user.CreateUserHandler;
import ru.gelman.core.request.handler.user.LoginHandler;
import ru.gelman.repository.ChatRepository;
import ru.gelman.repository.h2.ChatH2Repository;
import ru.gelman.service.ChatService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ChatServerApplication {
    private static final Properties CONFIG = PropertyLoader.load("/server_config.properties");

    public static void main(String[] args) {
        try {
            log.info("initializing service");
            ChatRepository repository = new ChatH2Repository();
            ChatService service = new ChatService(repository);
            ChatController controller = new ChatController(service);

            log.info("initializing handlers");
            Map<ChatAction, ChatRequestHandler> handlers = new HashMap<>();
            handlers.put(ChatAction.USER_CREATE, new CreateUserHandler(controller));
            handlers.put(ChatAction.USER_LOGIN, new LoginHandler(controller));
            handlers.put(ChatAction.CHAT_CREATE, new CreateChatHandler(controller));
            handlers.put(ChatAction.MESSAGE_CREATE, new AddMessageHandler(controller));
            handlers.put(ChatAction.USER_GET, new GetUserChatsHandler(controller));
            handlers.put(ChatAction.SESSION_ACTIVATE, new ActivateSessionHandler(controller));

            log.info("initializing server components");
            TcpClientManager sessionManager = new TcpClientManager();
            int threads = Integer.parseInt(CONFIG.getProperty("threads"));

            log.info("initializing {} thread workers", threads);
            ExecutorService handleThreads = Executors.newFixedThreadPool(threads);

            int port = Integer.parseInt(CONFIG.getProperty("port"));
            log.info("starting server endpoint on port {}", port);
            try (ServerSocket server = new ServerSocket(port)) {
                while (true) {
                    Socket clientSocket = server.accept();
                    log.info("accept socket. ip: {}, port: {}", clientSocket.getInetAddress(), clientSocket.getPort());
                    Runnable mainHandler = new ChatServerRequestHandler(handlers, sessionManager, clientSocket);
                    handleThreads.submit(mainHandler);
                }
            } catch (IOException e) {
                log.error("Server socket error: {}. Server shut down", e.getMessage());
            }
        } catch (RuntimeException e) {
            log.error("server error: {}. server shut down", e.getMessage());
        }
    }
}