package ru.gelman.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gelman.Message;
import ru.gelman.store.MessageStore;

import java.util.function.Supplier;

public class ProduceTask implements Runnable {
    private final Supplier<Message> producer;
    private final MessageStore store;
    private final long produceInterval;
    private final Logger logger = LoggerFactory.getLogger(ProduceTask.class);

    public ProduceTask(Supplier<Message> producer, MessageStore store, long produceInterval) {
        this.producer = producer;
        this.store = store;
        this.produceInterval = produceInterval;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = producer.get();
                store.put(message);
                logger.info("put " + message);
                Thread.sleep(produceInterval);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
