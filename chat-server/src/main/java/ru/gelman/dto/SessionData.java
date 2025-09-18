package ru.gelman.dto;

public record SessionData(UserData user, String sessionId, String expiredDate) {
}
