package ru.gelman.exceptions;

public class UnexpectedTokenException extends IllegalArgumentException {
    public UnexpectedTokenException(String token) {
        super(token);
    }
}
