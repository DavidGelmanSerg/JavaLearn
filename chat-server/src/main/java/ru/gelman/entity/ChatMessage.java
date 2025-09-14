package ru.gelman.entity;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ChatMessage implements Comparable<ChatMessage> {
    @ToString.Include
    private final int chatId;
    @ToString.Include
    private final Integer creatorId;
    @NonNull
    private final LocalDateTime creationDateTime;
    @Setter
    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer id;
    @Setter
    @NonNull
    private String content;
    @Setter
    @ToString.Include
    private boolean deleted;

    @Override
    public int compareTo(ChatMessage o) {
        return creationDateTime.compareTo(o.creationDateTime);
    }
}
