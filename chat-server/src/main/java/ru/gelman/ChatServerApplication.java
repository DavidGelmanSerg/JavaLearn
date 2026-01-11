package ru.gelman;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import ru.gelman.controller.ChatController;
import ru.gelman.controller.SessionController;
import ru.gelman.controller.UserController;
import ru.gelman.network.client.TcpClient;
import ru.gelman.core.ClientManager;
import ru.gelman.core.loader.ChatRequestHandlerLoader;
import ru.gelman.core.request.ChatRequestContext;
import ru.gelman.core.request.ChatServerRequestHandler;
import ru.gelman.core.request.handler.ChatRequestHandler;
import ru.gelman.mapper.ChatMapper;
import ru.gelman.mapper.MessageMapper;
import ru.gelman.mapper.SessionMapper;
import ru.gelman.mapper.UserMapper;
import ru.gelman.repository.ChatRepository;
import ru.gelman.repository.SessionRepository;
import ru.gelman.repository.UserRepository;
import ru.gelman.repository.config.DataBaseConfig;
import ru.gelman.repository.h2.H2ChatRepository;
import ru.gelman.repository.h2.H2SessionRepository;
import ru.gelman.repository.h2.H2UserRepository;
import ru.gelman.service.ChatService;
import ru.gelman.service.SessionService;
import ru.gelman.service.UserService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ChatServerApplication {
    public static void main(String[] args) {
        try {
            log.info("configs initialization");
            Properties serviceConfig = PropertyLoader.load("/service_config.properties");
            Properties databaseConfig = PropertyLoader.load("/db/liquibase.properties");
            Properties serverConfig = PropertyLoader.load("/server_config.properties");
            DataBaseConfig repositoryConfig = DataBaseConfig.from(databaseConfig);

            log.info("repositories initialization");
            UserRepository userRepository = new H2UserRepository(repositoryConfig);
            SessionRepository sessionRepository = new H2SessionRepository(repositoryConfig);
            ChatRepository chatRepository = new H2ChatRepository(repositoryConfig);

            log.info("services initialization");
            UserService userService = new UserService(userRepository);
            SessionService sessionService = new SessionService(sessionRepository, userRepository, serviceConfig);
            ChatService chatService = new ChatService(chatRepository);

            log.info("entity-dto mappers initialization");
            UserMapper userMapper = Mappers.getMapper(UserMapper.class);
            SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);
            ChatMapper chatMapper = Mappers.getMapper(ChatMapper.class);
            MessageMapper messageMapper = Mappers.getMapper(MessageMapper.class);

            log.info("controllers initialization");
            UserController userController = new UserController(userService, sessionService, userMapper);
            SessionController sessionController = new SessionController(sessionService, chatService, sessionMapper);
            ChatController chatController = new ChatController(chatService, userService, sessionService, chatMapper, messageMapper, userMapper);

            log.info("request handlers initialization");
            String handlersPackage = serverConfig.getProperty("handlers_package");
            List<ChatRequestHandler> handlersList = ChatRequestHandlerLoader.loadHandlersFromClassPath(handlersPackage);

            log.info("server components initialization");
            ClientManager sessionManager = new ClientManager();
            int threads = Integer.parseInt(serverConfig.getProperty("threads"));

            log.info("initialization of {} thread workers", threads);
            ExecutorService handleThreads = Executors.newFixedThreadPool(threads);

            int port = Integer.parseInt(serverConfig.getProperty("port"));
            log.info("startup server endpoint on port {}", port);
            while (true) {
                try (ServerSocket server = new ServerSocket(port)) {
                    Socket clientSocket = server.accept();
                    log.info("accept socket. ip: {}, port: {}", clientSocket.getInetAddress(), clientSocket.getPort());
                    TcpClient client = new TcpClient(clientSocket);

                    ChatRequestContext context = ChatRequestContext.builder()
                            .chatController(chatController)
                            .sessionController(sessionController)
                            .userController(userController)
                            .client(client)
                            .sessionManager(sessionManager)
                            .build();

                    handleThreads.submit(new ChatServerRequestHandler(handlersList, context));
                } catch (IOException e) {
                    log.warn("Server socket error: {}. Server shut down", e.getMessage());
                }
            }
        } catch (RuntimeException e) {
            log.error("server error: {}. server shut down", e.getMessage());
        }
    }
}