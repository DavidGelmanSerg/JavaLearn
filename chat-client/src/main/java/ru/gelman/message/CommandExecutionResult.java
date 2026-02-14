package ru.gelman.message;

public record CommandExecutionResult(boolean success, String message) {
}
