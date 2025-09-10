package ru.gelman.service.exception;

public class SessionNotFoundException extends ChatServiceException {
    public SessionNotFoundException(String sessionId) {
        super("SESSION_INACTIVE", String.format("session %s is inactive", sessionId));
    }
}
