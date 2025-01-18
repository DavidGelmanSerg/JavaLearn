package ru.gelman.store;

import ru.gelman.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueMessageStore implements MessageStore {
    private final BlockingQueue<Message> store;

    public BlockingQueueMessageStore(int capacity) {
        store = new LinkedBlockingQueue<>(capacity);
    }

    @Override
    public void put(Message message){
        try {
            store.put(message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public Message get() {
        try {
            return store.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }
}
