package ru.gelman.service.exception;

import lombok.Getter;

@Getter
public class ChatServiceException extends RuntimeException {
    private final String errorType;

    public ChatServiceException(String errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

}
