package ru.gelman.core.loader;

import lombok.extern.slf4j.Slf4j;
import ru.gelman.core.request.handler.ChatRequestHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ChatRequestHandlerLoader {
    private static ChatRequestHandler initImplementation(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            return (ChatRequestHandler) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            return null;
        }
    }

    public static List<ChatRequestHandler> loadHandlersFromClassPath(String packageName) {
        Set<Class<?>> implementations = PackageScanner.loadImplementations(packageName, ChatRequestHandler.class);
        return implementations.stream()
                .map(ChatRequestHandlerLoader::initImplementation)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
