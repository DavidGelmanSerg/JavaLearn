package ru.gelman.message;

import ru.gelman.Message;

public interface MessageStore {
    void put(Message message) throws InterruptedException;

    Message get() throws InterruptedException;
}
