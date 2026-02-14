package ru.gelman.dto;

public record CreateMessageRq(String content, String creationDateTime, int creatorId, int chatId) {
}
