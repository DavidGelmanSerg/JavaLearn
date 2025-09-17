package ru.gelman.entity;

import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ChatSession {
    @EqualsAndHashCode.Include
    private final String sessionId;
    private final ChatUser user;
    @Setter
    private LocalDateTime expiredDate;
}
