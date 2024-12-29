package ru.gelman.exceptions;

public class WritingFileFailureException extends RuntimeException {
    public WritingFileFailureException(String message) {
        super(message);
    }
}
