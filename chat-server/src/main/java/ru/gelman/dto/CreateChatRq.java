package ru.gelman.dto;

import java.util.List;

public record CreateChatRq(String name, int creatorId, List<Integer> userIds) {
}