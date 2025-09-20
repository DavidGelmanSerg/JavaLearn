package ru.gelman.entity;

import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatInfo {
    private final int creatorId;
    @EqualsAndHashCode.Include
    @Setter
    private Integer id;
    @NonNull
    @Setter
    private String name;
    @Setter
    private boolean deleted;
}
