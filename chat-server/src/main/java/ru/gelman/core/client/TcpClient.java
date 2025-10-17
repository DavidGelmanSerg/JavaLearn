package ru.gelman.core.client;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.core.request.ChatRequest;
import ru.gelman.core.response.ChatResponse;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
public class TcpClient implements Consumer<ChatResponse>, Supplier<ChatRequest> {
    private static final String READY_ACTION = "READY";
    private final Socket clientSocket;
    private InetSocketAddress destination;


    public TcpClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void accept(ChatResponse chatResponse) {
        try {
            log.info("connecting to client {}", destination);
            try (Socket client = new Socket(destination.getAddress(), destination.getPort());
                 var out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
                 var in = new Scanner(client.getInputStream(), StandardCharsets.UTF_8)
            ) {
                String json = chatResponse.toString();
                log.debug("accept response: {}", json);
                if (in.hasNextLine()) {
                    String line = in.nextLine();
                    if (line.equals(READY_ACTION)) {
                        out.println(json);
                        log.info("sending response to client {}", destination);
                    } else {
                        log.warn("expected READY action, got {} instead", line);
                        throw new RuntimeException("READY action not accepted from client");
                    }
                } else {
                    log.warn("no action got from client. response won't be sent");
                    throw new RuntimeException("no action got from client");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ChatRequest get() {
        try (var in = new Scanner(clientSocket.getInputStream(), StandardCharsets.UTF_8)) {
            var out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            log.info("sending {} to client", READY_ACTION);
            out.println(READY_ACTION);
            String json = null;
            if (in.hasNextLine()) {
                json = in.nextLine();
            }
            log.info("got data from client");
            log.debug("data: {}", json);

            clientSocket.close();

            ChatRequest request = new ChatRequest(json);
            setDestination(request);
            return request;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDestination(ChatRequest request) {
        String host = request.getHeader("host");
        int port = Integer.parseInt(request.getHeader("port"));
        log.info("setting destination address {}:{} for client: {}:{}", host, port, clientSocket.getInetAddress(), clientSocket.getPort());
        destination = new InetSocketAddress(host, port);
    }
}
