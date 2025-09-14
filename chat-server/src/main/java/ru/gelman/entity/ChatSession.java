package ru.gelman.entity;

import lombok.*;

@ToString
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ChatSession {
    @EqualsAndHashCode.Include
    private final String sessionId;
    private final ChatUser user;
    @Setter
    private boolean isActive;
}
