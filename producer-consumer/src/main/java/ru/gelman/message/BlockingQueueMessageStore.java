package ru.gelman.message;

import ru.gelman.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueMessageStore implements MessageStore {
    private final BlockingQueue<Message> store;

    public BlockingQueueMessageStore(int capacity) {
        store = new LinkedBlockingQueue<>(capacity);
    }

    @Override
    public void put(Message message) throws InterruptedException {
        store.put(message);
    }

    @Override
    public Message get() throws InterruptedException {
        return store.take();
    }
}
