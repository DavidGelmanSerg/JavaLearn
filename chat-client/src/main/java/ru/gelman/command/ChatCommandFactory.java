package ru.gelman.command;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ChatCommandFactory {

    private final Map<ChatAction, ChatCommand> commands;

    public ChatCommandFactory(Object... args) {
        commands = initCommands(List.of(args));
    }

    public ChatCommand getCommand(ChatAction action) {
        return commands.get(action);
    }

    private Map<ChatAction, ChatCommand> initCommands(List<?> args) {
        List<Class<?>> argTypes = args.stream().map(Object::getClass).collect(Collectors.toList());
        Map<ChatAction, ChatCommand> commands = new EnumMap<>(ChatAction.class);
        for (ChatAction action : ChatAction.values()) {
            try {
                Class<? extends ChatCommand> commandClass = action.getCommandClass();
                log.debug("Attempt to get constructor for class {} and argTypes: {}", commandClass, argTypes);
                Constructor<?> constructor = getConstructor(commandClass, argTypes);
                List<?> arguments = getArgumentsForConstructor(constructor, args);
                log.debug("Creating command with constructor: {} and args: {}", constructor, arguments);
                ChatCommand command = (ChatCommand) constructor.newInstance(arguments.toArray());
                log.info("Successfully created command {}", command);
                commands.put(action, command);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException | IllegalArgumentException e) {
                log.warn("Command {} not initialized. Caused by: {}", action, e.getMessage());
            }
        }
        return commands;
    }

    private Constructor<?> getConstructor(Class<?> clazz, List<Class<?>> argTypes) throws NoSuchMethodException {
        Constructor<?> constructor = Arrays.stream(clazz.getConstructors())
                .filter(c -> hasCorrectArgs(c, argTypes))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("constructor not found"));

        log.debug("found constructor {} with args count {} and types {}", constructor, constructor.getParameterCount(), constructor.getParameterTypes());
        return constructor;
    }

    private List<?> getArgumentsForConstructor(Constructor<?> constructor, List<?> args) {
        List<Class<?>> neededArgTypes = args.stream()
                .map(Object::getClass)
                .filter(aType -> Arrays.stream(constructor.getParameterTypes()).anyMatch(cParamType -> cParamType.isAssignableFrom(aType)))
                .collect(Collectors.toList());
        return args.stream().filter(a -> neededArgTypes.contains(a.getClass())).collect(Collectors.toList());
    }

    private boolean hasCorrectArgs(Constructor<?> constructor, List<Class<?>> argTypes) {
        return Arrays.stream(constructor.getParameterTypes())
                .allMatch(paramType -> argTypes.stream().anyMatch(paramType::isAssignableFrom));
    }
}
