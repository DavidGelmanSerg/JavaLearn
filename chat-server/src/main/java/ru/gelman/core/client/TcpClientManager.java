package ru.gelman.core.client;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TcpClientManager {
    private final Map<String, TcpClient> sessions;

    public TcpClientManager() {
        sessions = new ConcurrentHashMap<>();
    }

    public TcpClient getClient(String sessionId) {
        return sessions.get(sessionId);
    }

    public void setSession(String sessionId, TcpClient client) {
        if (!hasClient(client) && !hasSession(sessionId)) {
            sessions.put(sessionId, client);
        }
    }

    public boolean hasClient(TcpClient client) {
        return sessions.containsValue(client);
    }

    public boolean hasSession(String sessionId) {
        return sessions.keySet().stream().anyMatch(key -> key.equals(sessionId));
    }
}
