package ru.gelman.service.exception;

public class LoginFailedException extends ChatServiceException{
    public LoginFailedException() {
        super("LOGIN FAILED", "invalid user name or password");
    }
}
