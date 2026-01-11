package ru.gelman.command;

import lombok.Getter;

@Getter
public enum ChatAction {
    USER_CREATE(CreateUserCommand.class),
    USER_LOGIN(LoginUserCommand.class),
    HELP(HelpUserCommand.class),
    CHAT_CREATE(CreateChatHandler.class);

    private final Class<? extends ChatCommand> commandClass;

    ChatAction(Class<? extends ChatCommand> commandClass) {
        this.commandClass = commandClass;
    }
}

