package ru.gelman.service.exception;

public class UserNotFoundException extends ChatServiceException {
    public UserNotFoundException(int id) {
        super("USER_NOT_FOUND", String.format("user with id %d not found", id));
    }
}
