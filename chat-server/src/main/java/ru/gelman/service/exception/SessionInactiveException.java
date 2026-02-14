package ru.gelman.service.exception;

public class SessionInactiveException extends ChatServiceException {
    public SessionInactiveException(String sessionId) {
        super("SESSION_NOT_FOUND", String.format("session %s inactive", sessionId));
    }
}
