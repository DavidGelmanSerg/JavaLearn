package ru.gelman.store;

import ru.gelman.Message;

public interface MessageStore {
    void put(Message message);

    Message get();
}
