package ru.gelman.entity;

import lombok.*;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ChatUser {
    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer id;
    @ToString.Include
    private String name;
    private String password;
    @ToString.Include
    private boolean deleted;
}
