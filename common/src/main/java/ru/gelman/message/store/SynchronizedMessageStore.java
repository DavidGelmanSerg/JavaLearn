package ru.gelman.message.store;

import java.util.ArrayDeque;
import java.util.Queue;

public class SynchronizedMessageStore<T> implements MessageStore<T> {
    private final Queue<T> messagesQueue;
    private final int capacity;

    public SynchronizedMessageStore(int capacity) {
        this.capacity = capacity;
        messagesQueue = new ArrayDeque<>(capacity);
    }

    @Override
    public synchronized void put(T message) throws InterruptedException {
        while (messagesQueue.size() == capacity) {
            wait();
        }
        messagesQueue.add(message);
        notifyAll();
    }

    @Override
    public synchronized T get() throws InterruptedException {
        while (messagesQueue.isEmpty()) {
            wait();
        }
        notifyAll();
        return messagesQueue.poll();
    }
}
