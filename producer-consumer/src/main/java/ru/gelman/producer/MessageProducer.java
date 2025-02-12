package ru.gelman.producer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gelman.Message;
import ru.gelman.store.MessageStore;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class MessageProducer implements Supplier<Message>, Runnable {
    private static final AtomicLong nextId = new AtomicLong();
    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    private final MessageStore store;
    private final long produceDelay;

    @Override
    public Message get() {
        long id = nextId.incrementAndGet();
        return new Message(id);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = this.get();
                store.put(message);
                logger.info("put " + message);
                Thread.sleep(produceDelay);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
