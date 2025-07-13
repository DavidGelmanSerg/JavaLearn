package ru.gelman.store;

import ru.gelman.Message;

import java.util.ArrayDeque;
import java.util.Queue;

public class SynchronizedMessageStore implements MessageStore {
    private final Queue<Message> messagesQueue;
    private final int capacity;

    public SynchronizedMessageStore(int capacity) {
        this.capacity = capacity;
        messagesQueue = new ArrayDeque<>(capacity);
    }

    @Override
    public synchronized void put(Message message) throws InterruptedException {
        while (messagesQueue.size() == capacity) {
            wait();
        }
        messagesQueue.add(message);
        notifyAll();
    }

    @Override
    public synchronized Message get() throws InterruptedException {
        while (messagesQueue.isEmpty()) {
            wait();
        }
        notifyAll();
        return messagesQueue.poll();
    }
}
