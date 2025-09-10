package ru.gelman.service.exception;

import ru.gelman.entity.ChatUser;

public class UserAlreadyExistsException extends ChatServiceException {
    public UserAlreadyExistsException(ChatUser user) {
        super("USER ALREADY EXISTS", String.format("user with name %s already exists", user.getName()));
    }
}
