package ru.gelman.network.client;

import ru.gelman.network.data.NetMessage;

import java.io.Closeable;

public interface Client extends Closeable {
    NetMessage getResponse(NetMessage request);

    void sendMessage(NetMessage message);

    NetMessage getMessage();

    void setTimeout(int timeoutMillis);

    boolean isClosed();
}
