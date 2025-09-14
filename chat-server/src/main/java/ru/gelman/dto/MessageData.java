package ru.gelman.dto;

public record MessageData(int messageId, int chatId, int creatorId, String content, String creationDateTime) {
}
