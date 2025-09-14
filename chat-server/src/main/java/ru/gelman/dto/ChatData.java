package ru.gelman.dto;


import java.util.List;

public record ChatData(ChatInfoData info, List<UserData> users, List<MessageData> messages) {
}
