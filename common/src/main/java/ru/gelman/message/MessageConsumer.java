package ru.gelman.message;

public interface MessageConsumer<T> {
    void accept(T message);
}
