package ru.gelman.core;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.network.client.Client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ClientManager {
    private final Map<String, Client> sessions;

    public ClientManager() {
        sessions = new ConcurrentHashMap<>();
    }

    public Client getClient(String sessionId) {
        return sessions.get(sessionId);
    }

    public void setSession(String sessionId, Client client) {
        if (!hasClient(client) && !hasSession(sessionId)) {
            sessions.put(sessionId, client);
        }
    }

    public boolean hasClient(Client client) {
        return sessions.containsValue(client);
    }

    public boolean hasSession(String sessionId) {
        return sessions.keySet().stream().anyMatch(key -> key.equals(sessionId));
    }
}
