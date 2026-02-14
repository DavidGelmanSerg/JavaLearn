package ru.gelman.message.store;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueMessageStore<T> implements MessageStore<T> {
    private final BlockingQueue<T> store;

    public BlockingQueueMessageStore(int capacity) {
        store = new LinkedBlockingQueue<>(capacity);
    }

    @Override
    public void put(T message) throws InterruptedException {
        store.put(message);
    }

    @Override
    public T get() throws InterruptedException {
        return store.take();
    }
}
