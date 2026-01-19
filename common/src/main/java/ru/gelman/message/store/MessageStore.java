package ru.gelman.message.store;

public interface MessageStore<T> {
    void put(T data) throws InterruptedException;

    T get() throws InterruptedException;

}
