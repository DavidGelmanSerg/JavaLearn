package ru.gelman.network.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.gelman.network.data.JsonNetMessage;
import ru.gelman.network.data.NetMessage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class TcpClient implements Client {
    private final Socket socket;

    public TcpClient(Socket socket, int timeoutMillis) throws IOException {
        this.socket = socket;
        socket.setSoTimeout(timeoutMillis);
    }

    @Override
    public NetMessage getResponse(NetMessage request) {
        sendMessage(request);
        return getMessage();
    }

    @Override
    public synchronized void sendMessage(NetMessage message) {
        try {
            var out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            String messageString = message.asStringValue();
            log.info("sending message: {}", messageString);
            out.println(messageString);
        } catch (IOException e) {
            log.error("could not send message to socket {}. Reason: {}", socket.getInetAddress(), e.getMessage());
            throw new RuntimeException("could not send message to socket");
        }
    }

    @Override
    public synchronized NetMessage getMessage() {
        try {
            var in = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
            if (in.hasNextLine()) {
                String message = in.nextLine();
                log.info("got message: {}", message);
                return new JsonNetMessage(message);
            }
            log.error("no response from socket {}", socket.getInetAddress());
            throw new RuntimeException("no response from socket");
        } catch (IOException e) {
            log.error("could not send message to socket {}. Reason: {}", socket.getInetAddress(), e.getMessage());
            throw new RuntimeException("could not send message to socket");
        }
    }

    @Override
    public synchronized void setTimeout(int timeoutMillis) {
        try {
            socket.setSoTimeout(timeoutMillis);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public synchronized void close() throws IOException {
        socket.close();
    }
}
