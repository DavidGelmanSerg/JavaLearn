package ru.gelman.consumer;

import ru.gelman.Message;
import ru.gelman.store.MessageStore;

import java.util.function.Consumer;

public class ConsumeTask implements Runnable {
    private final Consumer<Message> consumer;
    private final MessageStore store;
    private final long consumeInterval;
    public ConsumeTask(Consumer<Message> consumer, MessageStore store, long consumeInterval) {
        this.consumer = consumer;
        this.store = store;
        this.consumeInterval = consumeInterval;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = store.get();
                consumer.accept(message);
                Thread.sleep(consumeInterval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
