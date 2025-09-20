package ru.gelman.dto;

public record MessageData(int messageId, int chatId, UserData creator, String content, String creationDateTime) {
}
