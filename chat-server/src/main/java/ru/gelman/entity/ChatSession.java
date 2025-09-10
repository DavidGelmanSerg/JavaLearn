package ru.gelman.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatSession {
    @EqualsAndHashCode.Include
    private final String sessionId;
    private final ChatUser user;
    @Setter
    private boolean isActive;

    public ChatSession(String sessionId, ChatUser chatUser) {
        this.sessionId = sessionId;
        this.user = chatUser;
        this.isActive = true;
    }
}
